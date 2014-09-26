package com.abien.xray.business.hits.control;

import com.abien.xray.business.hits.entity.CacheValue;
import com.abien.xray.business.hits.entity.Hit;
import com.abien.xray.business.hits.entity.Post;
import com.abien.xray.business.logging.boundary.XRayLogger;
import com.abien.xray.business.monitoring.PerformanceAuditor;
import com.abien.xray.business.statistics.entity.DailyHits;
import com.airhacks.xray.grid.control.Grid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;
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
import javax.json.Json;
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

    @Inject
    XRayLogger LOG;

    public static final String ENTRY = "/entry/";

    @Inject
    URLFilter urlFilter;

    @Inject
    Event<String> uriListener;

    private HitsCache hitCache = null;
    private HitsCache trendingCache = null;
    private HitsCache refererCache;

    private DailyHitCache dailyHitCache;

    @Inject
    @Grid(Grid.Name.HITS)
    private ConcurrentMap<String, String> hits;

    @Inject
    @Grid(Grid.Name.REJECTED)
    private Queue<String> rejected;

    @Inject
    @Grid(Grid.Name.DAILY)
    private Map<String, String> daily;

    @Inject
    @Grid(Grid.Name.FILTERS)
    private Map<String, String> filters;

    @Inject
    @Grid(Grid.Name.TRENDING)
    private ConcurrentMap<String, String> trending;

    @Inject
    @Grid(Grid.Name.REFERERS)
    private ConcurrentMap<String, String> referers;

    @Inject
    private FilterProvider provider;

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
            if (urlFilter.ignore(uri)) {
                LOG.log(Level.INFO, "updateStatistics - URL: {0} is rejected by urlFilter with headers {1}", new Object[]{uri, headerMap});
                this.rejected.add(serialize(uri, referer, headerMap));
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
        return this.refererCache.increase(referer);
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
        return this.hitCache.getTotalHits();
    }

    public long totalTrending() {
        return this.trendingCache.getTotalHits();
    }

    public List<Post> getTrending() {
        List<Post> trends = new ArrayList<>();
        Map<String, String> cache = trendingCache.getCache();
        Set<Map.Entry<String, String>> trendEntries = cache.entrySet();
        trendEntries.stream().map((trendEntry) -> {
            String hitsValue = trendEntry.getValue();
            Post post = new Post(trendEntry.getKey(), hitsValue);
            return post;
        }).forEach((post) -> {
            trends.add(post);
        });
        Collections.sort(trends, Collections.reverseOrder());
        return trends;
    }

    public String totalHitsAsString() {
        return String.valueOf(totalHits());
    }

    public List<CacheValue> topReferers(String excludeContaining, int maxNumber) {
        return this.refererCache.getMostPopularValuesNotContaining(excludeContaining, maxNumber);
    }

    public List<CacheValue> topReferers(int maxNumber) {
        return this.refererCache.getMostPopularValues(maxNumber, f -> true);
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
        String script = getScriptContent();
        Predicate<Map.Entry<String, String>> filter = this.provider.createFromNashornScript(script);
        return this.hitCache.getMostPopularValues(max, filter).
                stream().
                map(s -> new Hit(s.getRefererUri(), s.getCount())).
                collect(Collectors.toList());
    }

    String getScriptContent() {
        String scriptAsJson = this.filters.get("mostPopularPosts");
        LOG.log(Level.FINE, "Got script {0}", new Object[]{scriptAsJson});
        JsonObject object = JsonSerializer.deserialize(scriptAsJson);
        String script = null;
        if (object != null) {
            script = object.getString("script");
            LOG.log(Level.FINE, "Script {0} extracted", new Object[]{script});

        }
        return script;
    }

    String serialize(String uri, String referer, JsonObject headerMap) {
        JsonObject object = Json.createObjectBuilder().
                add("uri", uri).
                add("referer", referer).
                add("header", headerMap).build();
        return JsonSerializer.serialize(object);
    }

}
