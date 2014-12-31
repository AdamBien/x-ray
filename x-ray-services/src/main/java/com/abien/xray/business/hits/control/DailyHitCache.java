package com.abien.xray.business.hits.control;

import com.abien.xray.business.statistics.entity.DailyHits;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.cache.Cache;

/**
 *
 * @author adam-bien.com
 */
public class DailyHitCache {

    private final Comparator<Cache.Entry<String, String>> decreasing = (l, r) -> l.getValue().compareTo(r.getValue());
    private final Cache<String, String> dailyHits;

    public DailyHitCache(Cache<String, String> dailyHits) {
        this.dailyHits = dailyHits;
    }

    public List<DailyHits> getDailyHits() {
        return StreamSupport.stream(this.dailyHits.spliterator(), false).
                sorted(decreasing).
                map(s -> new DailyHits(s.getKey(), s.getValue())).
                collect(Collectors.toList());
    }

    public void save(DailyHits hit) {
        this.dailyHits.put(String.valueOf(hit.getEpoch()), String.valueOf(hit.getHit()));
    }
}
