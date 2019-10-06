package com.abien.xray.business.hits.control;

import com.abien.xray.business.hits.entity.CacheValue;
import com.abien.xray.business.hits.entity.Hit;
import com.abien.xray.business.hits.entity.Post;
import com.abien.xray.business.logging.boundary.XRayLogger;
import com.abien.xray.business.statistics.entity.DailyHits;

import io.netty.handler.logging.LogLevel;
import io.quarkus.scheduler.Scheduled;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;

/**
 * @author Adam Bien, blog.adam-bien.com
 */
@ApplicationScoped
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

    Map<String, Long> hits;
    Map<String, Long> daily;
    Map<String, Long> trending;

    Map<String, String> filters;
    Map<String, Long> referers;
    Queue<String> rejected;

    @Inject
    FilterProvider provider;

    @Inject
    ResultCache<Long> totalHitsCache;

    final static String HOUR = "1h";

    @PostConstruct
    public void preloadCache() {
        this.hits = new ConcurrentHashMap<>();
        this.daily = new ConcurrentHashMap<>();
        this.filters = new ConcurrentHashMap<>();
        this.trending = new ConcurrentHashMap<>();
        this.referers = new ConcurrentHashMap<>();
        this.rejected = new LinkedList<>();

        this.hitCache = new HitsCache(this.hits);
        this.trendingCache = new HitsCache(this.trending);
        this.refererCache = new HitsCache(this.referers);
        this.dailyHitCache = new DailyHitCache(this.daily);
    }

    @Scheduled(every = HOUR)
    public void resetTrends() {
        LOG.log(Level.INFO, "Resetting trends");
        trending.clear();
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

    public long updateHitsForURI(String uri, long hit) {
        return this.hits.merge(uri, hit, this::add);
    }

    long add(long old, long current) {
        return old + current;
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
        LOG.log(Level.INFO, "Storing: " + uniqueAction);
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
        return this.totalHitsCache.getCachedValueOr(this.hitCache::getTotalHits, 0l);
    }

    public long totalTrending() {
        return this.trendingCache.getTotalHits();
    }

    public List<Post> getTrending() {
        Map<String, Long> cache = trendingCache.getCache();
        List<Post> trends = StreamSupport.stream(cache.entrySet().spliterator(), false).map((trendEntry) -> {
            Post post = new Post(trendEntry.getKey(), trendEntry.getValue());
            return post;
        }).collect(Collectors.toList());
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

    public List<DailyHits> getDailyHits() {
        return this.dailyHitCache.getDailyHits();
    }

    public void save(DailyHits dailyHits) {
        this.dailyHitCache.save(dailyHits);

    }

    public List<Hit> getMostPopularPosts(int max) {
        String script = getScriptContent();
        Predicate<Map.Entry<String, Long>> filter = this.provider.createFromNashornScript(script);
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

    public Map<String, Long> getDaily() {
        return this.daily;
    }

    public Map<String, Long> getHits() {
        return this.hits;
    }

}
