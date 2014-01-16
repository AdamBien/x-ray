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

    private ConcurrentMap<String, AtomicLong> hits = null;
    /**
     * Left and right swapped on purpose -> decreasing order
     */
    private Comparator<Map.Entry<String, AtomicLong>> decreasing = (l, r) -> new Long(r.getValue().get()).compareTo(l.getValue().get());

    public HitsCache(ConcurrentMap hits) {
        this.hits = hits;
    }

    public long increase(String uri) {
        hits.putIfAbsent(uri, new AtomicLong());
        AtomicLong hitCount = hits.get(uri);
        long value = hitCount.incrementAndGet();
        hits.replace(uri, hitCount);
        return value;
    }

    public long getCount(String uri) {
        AtomicLong counter = hits.get(uri);
        if (counter == null) {
            return 0;
        } else {
            return counter.get();
        }
    }

    public Map<String, AtomicLong> getCache() {
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
                map(f -> new CacheValue(f.getKey(), f.getValue().get())).
                collect(Collectors.toList());
    }

    public List<CacheValue> getMostPopularValues(int maxNumber) {
        return this.hits.entrySet().parallelStream().
                sorted(decreasing).
                limit(maxNumber).
                map(f -> new CacheValue(f.getKey(), f.getValue().get())).
                collect(Collectors.toList());
    }

    public void clear() {
        hits.clear();
    }
}
