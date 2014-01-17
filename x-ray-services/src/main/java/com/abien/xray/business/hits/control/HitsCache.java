package com.abien.xray.business.hits.control;

import com.abien.xray.business.hits.entity.CacheValue;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * User: blog.adam-bien.com Date: 17.02.11 Time: 21:11
 */
public class HitsCache {

    private ConcurrentMap<String, String> hits = null;
    /**
     * Left and right swapped on purpose -> decreasing order
     */
    private final Comparator<Map.Entry<String, String>> decreasing = (l, r) -> new Long(Long.parseLong(r.getValue())).compareTo(Long.parseLong(l.getValue()));

    public HitsCache(ConcurrentMap hits) {
        this.hits = hits;
    }

    public long increase(String uri) {
        hits.putIfAbsent(uri, "0");
        String hitCountAsString = hits.get(uri);
        AtomicLong hitCount = new AtomicLong(Long.parseLong(hitCountAsString));
        long value = hitCount.incrementAndGet();
        hits.replace(uri, String.valueOf(hitCount));
        return value;
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

    public int getCacheSize() {
        return this.hits.size();
    }

    public List<CacheValue> getMostPopularValuesNotContaining(String excludeContaining, int maxNumber) {
        return this.hits.entrySet().
                parallelStream().
                filter(f -> !f.getKey().contains(excludeContaining)).
                sorted(decreasing).
                map(f -> new CacheValue(f.getKey(), f.getValue())).
                collect(Collectors.toList());
    }

    public List<CacheValue> getMostPopularValues(int maxNumber) {
        return this.hits.entrySet().parallelStream().
                sorted(decreasing).
                limit(maxNumber).
                map(f -> new CacheValue(f.getKey(), f.getValue())).
                collect(Collectors.toList());
    }

    public void clear() {
        hits.clear();
    }
}
