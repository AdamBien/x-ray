package com.abien.xray.business.hits.control;

import com.abien.xray.business.statistics.entity.DailyHits;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author adam-bien.com
 */
public class DailyHitCache {

    private final Comparator<Map.Entry<String, Long>> decreasing = (l, r) -> l.getValue().compareTo(r.getValue());
    private final Map<String, Long> dailyHits;

    public DailyHitCache(Map<String, Long> dailyHits) {
        this.dailyHits = dailyHits;
    }

    public List<DailyHits> getDailyHits() {
        return this.dailyHits.entrySet().stream().
                sorted(decreasing).
                map(s -> new DailyHits(s.getKey(), s.getValue())).
                collect(Collectors.toList());
    }

    public void save(DailyHits hit) {
        this.dailyHits.put(String.valueOf(hit.getDateAsString()), hit.getHit());
    }
}
