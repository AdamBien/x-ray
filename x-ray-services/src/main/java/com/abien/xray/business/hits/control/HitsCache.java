package com.abien.xray.business.hits.control;

import com.abien.xray.business.hits.entity.CacheValue;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * User: blog.adam-bien.com Date: 17.02.11 Time: 21:11
 */
public class HitsCache {

    private Map<String, String> hits = null;
    /**
     * Left and right swapped on purpose -> decreasing order
     */
    private final Comparator<Map.Entry<String, String>> decreasing = (l, r) -> new Long(Long.parseLong(r.getValue())).compareTo(Long.parseLong(l.getValue()));

    public HitsCache(Map<String, String> hits) {
        this.hits = hits;
    }

    public long increase(String uri) {
        hits.putIfAbsent(uri, "0");
        String hitCountAsString = hits.get(uri);
        AtomicLong hitCount = new AtomicLong(Long.parseLong(hitCountAsString));
        long value = hitCount.incrementAndGet();
        hits.replace(uri, String.valueOf(value));
        return value;
    }

    public void updateHitsForURI(String uri, String hit) {
        this.hits.put(uri, String.valueOf(hit));
    }

    public long getCount(String uri) {
        String counterAsString = hits.get(uri);
        if (counterAsString == null) {
            return 0;
        } else {
            return Long.parseLong(counterAsString);
        }
    }

    public Map<String, String> getCache() {
        return hits;
    }

    public long getCacheSize() {
        return this.hits.size();
    }

    public List<CacheValue> getMostPopularValuesNotContaining(String excludeContaining, int maxNumber) {
        return StreamSupport.stream(this.hits.entrySet().spliterator(), true).
                filter(f -> !f.getKey().contains(excludeContaining)).
                sorted(decreasing).
                map(f -> new CacheValue(f.getKey(), f.getValue())).
                collect(Collectors.toList());
    }

    public List<CacheValue> getMostPopularValues(int maxNumber, Predicate<Map.Entry<String, String>> filter) {
        return StreamSupport.stream(this.hits.entrySet().spliterator(), true).
                filter(filter).
                sorted(decreasing).
                limit(maxNumber).
                map(f -> new CacheValue(f.getKey(), f.getValue())).
                collect(Collectors.toList());
    }

    public long getTotalHits() {
        return StreamSupport.stream(this.hits.entrySet().spliterator(), false).mapToLong(v -> Long.parseLong(v.getValue())).sum();
    }

    public void clear() {
        hits.clear();
    }
}
