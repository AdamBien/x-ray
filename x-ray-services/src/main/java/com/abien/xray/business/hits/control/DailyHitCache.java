package com.abien.xray.business.hits.control;

import com.abien.xray.business.statistics.entity.DailyHits;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author adam-bien.com
 */
public class DailyHitCache {

    private final Comparator<Map.Entry<Date, Long>> decreasing = (l, r) -> l.getValue().compareTo(r.getValue());
    private final Map<Date, Long> dailyHits;

    public DailyHitCache(Map<Date, Long> dailyHits) {
        this.dailyHits = dailyHits;
    }

    public List<DailyHits> getDailyHits() {
        return this.dailyHits.entrySet().
                parallelStream().
                sorted(decreasing).
                map(s -> new DailyHits(s.getKey(), s.getValue())).
                collect(Collectors.toList());
    }

    public void save(DailyHits hit) {
        this.dailyHits.put(hit.getDate(), hit.getHit());
    }
}
