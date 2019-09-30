package com.abien.xray.business.hits.control;

import com.abien.xray.business.statistics.entity.DailyHits;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.enterprise.context.Dependent;

/**
 *
 * @author adam-bien.com
 */
@Dependent
public class DailyHitCache {

    private final Comparator<Map.Entry<String, String>> decreasing = (l, r) -> l.getValue().compareTo(r.getValue());
    private final Map<String, String> dailyHits;

    public DailyHitCache(Map<String, String> dailyHits) {
        this.dailyHits = dailyHits;
    }

    public List<DailyHits> getDailyHits() {
        return StreamSupport.stream(this.dailyHits.entrySet().spliterator(), false).
                sorted(decreasing).
                map(s -> new DailyHits(s.getKey(), s.getValue())).
                collect(Collectors.toList());
    }

    public void save(DailyHits hit) {
        this.dailyHits.put(String.valueOf(hit.getDateAsString()), String.valueOf(hit.getHit()));
    }
}
