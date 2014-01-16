package com.abien.xray.business.hits.control;

import com.abien.xray.business.grid.control.Grid;
import com.abien.xray.business.hits.entity.CacheValue;
import com.abien.xray.business.hits.entity.Hit;
import com.abien.xray.business.hits.entity.Post;
import com.abien.xray.business.logging.boundary.XRayLogger;
import com.abien.xray.business.monitoring.PerformanceAuditor;
import com.abien.xray.business.monitoring.entity.Diagnostics;
import com.abien.xray.business.statistics.entity.DailyHits;
import com.hazelcast.core.HazelcastInstance;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.json.JsonObject;

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
    URLFilter urlFilter;

    @Inject
    HttpHeaderFilter httpHeaderFilter;

    @Inject
    Event<Diagnostics> monitoring;

    @Inject
    Event<String> uriListener;

    private HitsCache hitCache = null;
    private HitsCache trendingCache = null;
    private HitsCache refererCache;

    private AtomicLong numberOfRejectedRequests = new AtomicLong(0);

    @Inject
    HazelcastInstance hazelcastInstance;
    private DailyHitCache dailyHitCache;

    @Inject
    @Grid(Grid.Name.HITS)
    private ConcurrentMap<String, Long> hits;

    @Inject
    @Grid(Grid.Name.DAILY)
    private Map<Date, Long> daily;

    @Inject
    @Grid(Grid.Name.TRENDING)
    private ConcurrentMap<String, Long> trending;

    @Inject
    @Grid(Grid.Name.REFERERS)
    private ConcurrentMap<String, Long> referers;

    @PostConstruct
    public void preloadCache() {
        this.hitCache = new HitsCache(this.hits);
        this.trendingCache = new HitsCache(this.trending);
        this.refererCache = new HitsCache(this.referers);
        this.dailyHitCache = new DailyHitCache(this.daily);
    }

    public void updateStatistics(String uri, String referer, JsonObject headerMap) {
        LOG.log(Level.INFO, "updateStatistics({0})", new Object[]{uri});
        try {
            if (urlFilter.ignore(uri)) {//|| httpHeaderFilter.ignore(headerMap)) {
                LOG.log(Level.INFO, "updateStatistics - URL: {0} is rejected by urlFilter with headers {1}", new Object[]{uri, headerMap});
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
        return this.hitCache.getCount(uri);
    }

    public long getHitsForURI(String uri) {
        return this.hitCache.getCount(uri);
    }

    void storeURI(String uri) {
        uriListener.fire(uri);
        storeHitStatistics(uri);
        if (isRelevantForTrend(uri)) {
            storeTrending(uri);
        }
    }

    long storeHitStatistics(String uniqueAction) {
        return this.hitCache.increase(uniqueAction);
    }

    long storeTrending(String uniqueAction) {
        return this.trendingCache.increase(uniqueAction);
    }

    long storeReferer(String referer) {
        return this.refererCache.increase(shortenReferer(referer, REFERER_MAX_LENGTH));
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
        return computeHits(this.hitCache.getCache());
    }

    public long totalTrending() {
        return computeHits(this.trendingCache.getCache());
    }

    public List<Post> getTrending() {
        List<Post> trends = new ArrayList<>();
        Map<String, AtomicLong> cache = trendingCache.getCache();
        Set<Map.Entry<String, AtomicLong>> trendEntries = cache.entrySet();
        trendEntries.stream().map((trendEntry) -> {
            long hitsValue = trendEntry.getValue().get();
            Post post = new Post(trendEntry.getKey(), hitsValue);
            return post;
        }).forEach((post) -> {
            trends.add(post);
        });
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

    public String totalHitsAsString() {
        return String.valueOf(totalHits());
    }

    public List<CacheValue> topReferers(String excludeContaining, int maxNumber) {
        return this.refererCache.getMostPopularValuesNotContaining(excludeContaining, maxNumber);
    }

    public List<CacheValue> topReferers(int maxNumber) {
        return this.refererCache.getMostPopularValues(maxNumber);
    }

    @Produces
    @Grid(Grid.Name.REFERERS)
    public HitsCache referersCache() {
        return this.refererCache;
    }

    @Produces
    @Grid(Grid.Name.HITS)
    public HitsCache statisticsCache() {
        return hitCache;
    }

    @Produces
    @Grid(Grid.Name.TRENDING)
    public HitsCache trendingCache() {
        return trendingCache;
    }

    public List<DailyHits> getDailyHits() {
        return this.dailyHitCache.getDailyHits();
    }

    public void save(DailyHits dailyHits) {
        this.dailyHitCache.save(dailyHits);

    }

    public List<Hit> getMostPopularPosts(int max) {
        return this.hitCache.getMostPopularValues(max).
                parallelStream().
                map(s -> new Hit(s.getRefererUri(), s.getCount())).
                collect(Collectors.toList());
    }

    public List<Hit> getMostPopularPostsNotContaining(String exclude, int max) {
        return this.hitCache.getMostPopularValuesNotContaining(exclude, max).
                parallelStream().
                map(s -> new Hit(s.getRefererUri(), s.getCount())).
                collect(Collectors.toList());
    }

}
