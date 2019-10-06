package com.abien.xray.business.hits.control;

import com.abien.xray.business.hits.entity.CacheValue;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * User: blog.adam-bien.com Date: 17.02.11 Time: 21:11
 */
public class HitsCache {

    private Map<String, Long> hits = null;
    /**
     * Left and right swapped on purpose -> decreasing order
     */
    private final Comparator<Map.Entry<String, Long>> decreasing = (l, r) -> r.getValue().compareTo(l.getValue());

    public HitsCache(Map<String, Long> hits) {
        this.hits = hits;
    }

    public long increase(String uri) {
        
        hits.putIfAbsent(uri, 0l);
        long hitsAsLong = hits.get(uri);
        AtomicLong hitCount = new AtomicLong(hitsAsLong);
        long value = hitCount.incrementAndGet();
        hits.put(uri, value);
        return value;
    }

    public void updateHitsForURI(String uri, long hits) {
        this.hits.put(uri, hits);
    }

    public long getCount(String uri) {
        return hits.getOrDefault(uri,0l);
    }

    public Map<String, Long> getCache() {
        return hits;
    }

    public long getCacheSize() {
        return this.hits.size();
    }

    public List<CacheValue> getMostPopularValuesNotContaining(String excludeContaining, int maxNumber) {
        return this.hits.entrySet().stream().
                filter(f -> !f.getKey().contains(excludeContaining)).
                sorted(decreasing).
                map(f -> new CacheValue(f.getKey(), f.getValue())).
                collect(Collectors.toList());
    }

    public List<CacheValue> getMostPopularValues(int maxNumber, Predicate<Entry<String, Long>> filter) {
        return this.hits.entrySet().stream().
                filter(filter).
                sorted(decreasing).
                limit(maxNumber).
                map(f -> new CacheValue(f.getKey(), f.getValue())).
                collect(Collectors.toList());
    }

    public long getTotalHits() {
        return this.hits.values().stream().mapToLong(l -> l).sum();
    }

    public void clear() {
        hits.clear();
    }
}
