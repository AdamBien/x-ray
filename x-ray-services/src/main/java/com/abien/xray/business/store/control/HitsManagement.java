package com.abien.xray.business.store.control;

import com.abien.xray.business.logging.boundary.XRayLogger;
import com.abien.xray.business.monitoring.PerformanceAuditor;
import com.abien.xray.business.monitoring.entity.Diagnostics;
import com.abien.xray.business.statistics.entity.DailyHits;
import com.abien.xray.business.store.boundary.Cache;
import com.abien.xray.business.store.entity.CacheValue;
import com.abien.xray.business.store.entity.Hit;
import com.abien.xray.business.store.entity.Post;
import com.abien.xray.business.useragent.control.UserAgentStatistics;
import com.hazelcast.core.HazelcastInstance;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

/**
 * @author Adam Bien, blog.adam-bien.com
 */
@LocalBean
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@Interceptors(PerformanceAuditor.class)
public class HitsManagement {

    public static final int REFERER_MAX_LENGTH = 250;

    @Inject
    XRayLogger LOG;

    public static final String ENTRY = "/entry/";
    @Inject
    UserAgentStatistics userAgentStatistics;

    @Inject
    URLFilter urlFilter;

    @Inject
    HttpHeaderFilter httpHeaderFilter;

    @Inject
    Event<Diagnostics> monitoring;

    @Inject
    Event<String> uriListener;

    private HitsCache hitStatistics = null;
    private HitsCache trending = null;
    private HitsCache refererStatistics = null;

    private AtomicLong numberOfRejectedRequests = new AtomicLong(0);

    @Inject
    HazelcastInstance hazelcastInstance;
    private DailyHitStore dailyHits;

    @PostConstruct
    public void preloadCache() {
        this.hitStatistics = new HitsCache(this.hazelcastInstance.getMap("hits"));
        this.refererStatistics = new HitsCache(this.hazelcastInstance.getMap("referers"));
        this.trending = new HitsCache(this.hazelcastInstance.getMap("trending"));
        this.dailyHits = new DailyHitStore(this.hazelcastInstance.getMap("trending"));
    }

    public void updateStatistics(String uri, String referer, Map<String, String> headerMap) {
        LOG.log(Level.INFO, "updateStatistics({0})", new Object[]{uri});
        try {
            /**
             * TODO testing
             * userAgentStatistics.extractAndStoreReferer(headerMap);
             *
             */
            if (urlFilter.ignore(uri) || httpHeaderFilter.ignore(headerMap)) {
                LOG.log(Level.INFO, "updateStatistics - URL: {0} is rejected with headers {1}", new Object[]{uri, headerMap});
                numberOfRejectedRequests.incrementAndGet();
                return;
            }
            storeURI(uri);
            if (referer != null && !referer.isEmpty()) {
                storeReferer(referer);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Problem processing request: {0} Because: {1}", new Object[]{uri, e});
        }
    }

    public long getCount(String uri) {
        return this.hitStatistics.getCount(uri);
    }

    @Schedule(hour = "*/1", persistent = false)
    public void resetTrends() {
        sendMonitoringData();
        trending.clear();
    }

    public long getHitsForURI(String uri) {
        return this.hitStatistics.getCount(uri);
    }

    void storeURI(String uniqueAction) {
        uriListener.fire(uniqueAction);
        storeHitStatistics(uniqueAction);
        if (isRelevantForTrend(uniqueAction)) {
            storeTrending(uniqueAction);
        }
    }

    long storeHitStatistics(String uniqueAction) {
        return this.hitStatistics.increase(uniqueAction);
    }

    long storeTrending(String uniqueAction) {
        return this.trending.increase(uniqueAction);
    }

    long storeReferer(String referer) {
        return this.refererStatistics.increase(shortenReferer(referer, REFERER_MAX_LENGTH));
    }

    String shortenReferer(String referer, int length) {
        if (referer != null && referer.length() > length) {
            return referer.substring(0, length);
        }
        return referer;
    }

    boolean isRelevantForTrend(String uniqueAction) {
        if (uniqueAction == null) {
            return false;
        }
        if (uniqueAction.contains(ENTRY + "url(")) {
            return false;
        }
        return uniqueAction.startsWith(ENTRY) && !uniqueAction.endsWith(ENTRY);
    }

    public long totalHits() {
        return computeHits(this.hitStatistics.getCache());
    }

    public long totalTrending() {
        return computeHits(this.trending.getCache());
    }

    public List<Post> getTrending() {
        List<Post> trends = new ArrayList<>();
        Map<String, AtomicLong> cache = trending.getCache();
        Set<Map.Entry<String, AtomicLong>> trendEntries = cache.entrySet();
        for (Map.Entry<String, AtomicLong> trendEntry : trendEntries) {
            long hits = trendEntry.getValue().get();
            Post post = new Post(trendEntry.getKey(), hits);
            trends.add(post);
        }
        Collections.sort(trends, Collections.reverseOrder());
        return trends;
    }

    private long computeHits(Map<String, AtomicLong> hits) {
        long totalCount = 0;
        Collection<AtomicLong> individualHits = hits.values();
        for (AtomicLong atomicLong : individualHits) {
            totalCount += atomicLong.get();
        }
        return totalCount;
    }

    void sendMonitoringData() {
        int hitCacheSize = this.hitStatistics.getCacheSize();
        int refererCacheSize = this.refererStatistics.getCacheSize();
        Diagnostics diagnostics = Diagnostics.with("hitCacheSize", hitCacheSize).
                and("refererCacheSize", refererCacheSize).
                and("numberOfRejectedRequests", this.numberOfRejectedRequests);
        monitoring.fire(diagnostics);

    }

    public String totalHitsAsString() {
        return String.valueOf(totalHits());
    }

    public List<CacheValue> topReferers(String excludeContaining, int maxNumber) {
        return this.refererStatistics.getMostPopularValuesNotContaining(excludeContaining, maxNumber);
    }

    public List<CacheValue> topReferers(int maxNumber) {
        return this.refererStatistics.getMostPopularValues(maxNumber);
    }

    @Produces
    @Cache(Cache.Name.REFERERS)
    public HitsCache referersCache() {
        return this.refererStatistics;
    }

    @Produces
    @Cache(Cache.Name.STATISTICS)
    public HitsCache statisticsCache() {
        return hitStatistics;
    }

    @Produces
    @Cache(Cache.Name.TRENDING)
    public HitsCache trendingCache() {
        return trending;
    }

    public List<DailyHits> getDailyHits() {
        return this.dailyHits.getDailyHits();
    }

    public void save(DailyHits dailyHits) {
        this.dailyHits.save(dailyHits);

    }

    public List<Hit> getMostPopularPosts(int max) {
        return this.hitStatistics.getMostPopularValues(max).
                parallelStream().
                map(s -> new Hit(s.getRefererUri(), s.getCount())).
                collect(Collectors.toList());
    }

    public List<Hit> getMostPopularPostsNotContaining(String exclude, int max) {
        return this.hitStatistics.getMostPopularValuesNotContaining(exclude, max).
                parallelStream().
                map(s -> new Hit(s.getRefererUri(), s.getCount())).
                collect(Collectors.toList());
    }

}
