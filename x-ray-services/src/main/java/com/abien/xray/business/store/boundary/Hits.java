package com.abien.xray.business.store.boundary;

import com.abien.xray.business.logging.boundary.XRayLogger;
import com.abien.xray.business.monitoring.PerformanceAuditor;
import com.abien.xray.business.monitoring.entity.Diagnostics;
import com.abien.xray.business.store.control.*;
import com.abien.xray.business.store.entity.Hit;
import com.abien.xray.business.store.entity.Post;
import com.abien.xray.business.store.entity.Referer;
import com.abien.xray.business.useragent.control.UserAgentStatistics;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Schedule;
import javax.ejb.Startup;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

/**
 * @author Adam Bien, blog.adam-bien.com
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@Interceptors(PerformanceAuditor.class)
public class Hits {
    public static final int REFERER_MAX_LENGTH = 250;

    @Inject
    XRayLogger LOG;

    public static final String ENTRY = "/entry/";
    @EJB
    PersistentHitStore hitStore;
    @EJB
    PersistentRefererStore refererStore;

    @EJB
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


    @PostConstruct
    public void preloadCache() {
        Map<String, AtomicLong> hits = hitStore.getHits();
        Map<String, AtomicLong> referers = refererStore.getReferers();
        hitStatistics = new HitsCache(hits);
        refererStatistics = new HitsCache(referers);
        trending = new HitsCache();
    }

    public void updateStatistics(String uri, String referer, Map<String, String> headerMap) {
        LOG.log(Level.INFO, "updateStatistics({0})", new Object[]{uri});
        try {
            /** TODO testing
             userAgentStatistics.extractAndStoreReferer(headerMap);
             **/
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

    public void persistHitsCache() {
        hitStore.store(hitStatistics.getChangedEntriesAndClear());
    }

    public void persistReferersCache() {
        refererStore.store(refererStatistics.getCache());
    }

    @Schedule(hour = "*/1", persistent = false)
    public void resetTrends() {
        sendMonitoringData();
        trending.clear();
    }

    public long getHitsForURI(String uri) {
        Hit hit = hitStore.find(uri);
        if (hit != null) {
            return hit.getCount();
        } else {
            return 0;
        }
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
        List<Post> trends = new ArrayList<Post>();
        ConcurrentHashMap<String, AtomicLong> cache = trending.getCache();
        Set<Map.Entry<String, AtomicLong>> trendEntries = cache.entrySet();
        for (Map.Entry<String, AtomicLong> trendEntry : trendEntries) {
            long hits = trendEntry.getValue().get();
            Post post = new Post(trendEntry.getKey(), hits);
            trends.add(post);
        }
        Collections.sort(trends, Collections.reverseOrder());
        return trends;
    }

    private long computeHits(ConcurrentHashMap<String, AtomicLong> hits) {
        long totalCount = 0;
        Collection<AtomicLong> individualHits = hits.values();
        for (AtomicLong atomicLong : individualHits) {
            totalCount += atomicLong.get();
        }
        return totalCount;
    }

    void sendMonitoringData() {
        int hitCacheSize = this.hitStatistics.getCacheSize();
        int hitDirtyEntriesCount = this.hitStatistics.getDirtyEntriesCount();

        int refererCacheSize = this.refererStatistics.getCacheSize();
        int refererDirtyEntriesCount = this.refererStatistics.getDirtyEntriesCount();

        Diagnostics diagnostics = Diagnostics.with("hitCacheSize", hitCacheSize).
                and("hitDirtyEntriesCount", hitDirtyEntriesCount).
                and("refererCacheSize", refererCacheSize).
                and("refererDirtyEntriesCount", refererDirtyEntriesCount).
                and("numberOfRejectedRequests", this.numberOfRejectedRequests);
        monitoring.fire(diagnostics);

    }

    public String totalHitsAsString() {
        return String.valueOf(totalHits());
    }

    public List<Referer> topReferers(String excludeContaining, int maxNumber) {
        return refererStore.getMostPopularReferersNotContaining(excludeContaining, maxNumber);
    }

    public List<Referer> topReferers(int maxNumber) {
        return refererStore.getMostPopularReferers(maxNumber);
    }
    
    
    @Produces @Cache
    public HitsCache refererStatistics(){
        return this.refererStatistics;
    }

    @PreDestroy
    public void saveTransientCache() {
        persistHitsCache();
        persistReferersCache();
    }
}
