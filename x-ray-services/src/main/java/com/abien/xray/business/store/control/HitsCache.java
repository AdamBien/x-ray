package com.abien.xray.business.store.control;

import com.abien.xray.business.store.entity.Referer;
import com.google.common.base.Suppliers;
import javafx.util.Pair;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * User: blog.adam-bien.com Date: 17.02.11 Time: 21:11
 */
public class HitsCache {

    private ConcurrentMap<String, AtomicLong> hits = null;

    public HitsCache(ConcurrentMap hits) {
        this.hits = hits;
    }

    public long increase(String uri) {
        hits.putIfAbsent(uri, new AtomicLong());
        AtomicLong hitCount = hits.get(uri);
        return hitCount.incrementAndGet();
    }

    public long getCount(String uri) {
        return hits.get(uri).get();
    }

    public Map<String, AtomicLong> getCache() {
        return hits;
    }

    public int getCacheSize() {
        return this.hits.size();
    }

    public List<Referer> getMostPopularReferersNotContaining(String excludeContaining, int maxNumber) {
        Comparator<Map.Entry<String,AtomicLong>> c = (l,r) -> new Long(l.getValue().get()).compareTo(r.getValue().get());
        return this.hits.entrySet().
                parallelStream().
                filter(f -> !f.getKey().contains(excludeContaining)).
                sorted(c).
                map(f -> new Referer(f.getKey(),f.getValue().get())).
                collect(Collectors.toList());
    }


    public List<Referer> getMostPopularReferers(int maxNumber) {
        Comparator<Map.Entry<String,AtomicLong>> c = (l,r) -> new Long(l.getValue().get()).compareTo(r.getValue().get());
        return this.hits.entrySet().parallelStream().
                sorted(c).
                limit(maxNumber).
                map(f -> new Referer(f.getKey(),f.getValue().get())).
                collect(Collectors.toList());
    }

    public void clear() {
        hits.clear();
    }

}
