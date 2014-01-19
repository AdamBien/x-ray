/*
 */
package com.abien.xray.business.statistics.control;

import com.abien.xray.business.hits.control.HitsManagement;
import com.abien.xray.business.logging.boundary.XRayLogger;
import com.abien.xray.business.statistics.entity.DailyHits;
import com.hazelcast.core.IAtomicLong;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.inject.Inject;

/**
 *
 * @author adam-bien.com
 */
public class DailyHitsCalculator {

    @Inject
    HitsManagement hits;

    @Inject
    IAtomicLong hitsAtMidnight;

    AtomicLong yesterdayHits;

    @Inject
    XRayLogger LOG;

    @PostConstruct
    public void initializeYesterday() {
        LOG.log(Level.INFO, "Initializing DailyStatisticsCalculator");
        final long yesterdayHitsValue = hitsAtMidnight.get();
        if (yesterdayHitsValue == 0) {
            LOG.log(Level.INFO, "Yesterday's hits are 0, overwriting with: " + yesterdayHitsValue);
            hitsAtMidnight.set(hits.totalHits());
        }
        this.yesterdayHits = new AtomicLong(0);
    }

    @Schedule(hour = "23", minute = "59", dayOfWeek = "*", dayOfMonth = "*", persistent = false)
    public void computeDailyHits() {
        AtomicLong todayHits = new AtomicLong(0);
        LOG.log(Level.INFO, "Computing daily hits");
        long totalHits = hits.totalHits();
        LOG.log(Level.INFO, "Total hits: " + totalHits);
        LOG.log(Level.INFO, "Yesterday's hits were: " + hitsAtMidnight.get());
        todayHits.set(totalHits - hitsAtMidnight.get());
        yesterdayHits.set(todayHits.get());
        LOG.log(Level.INFO, "Today hits: " + todayHits.get());
        hitsAtMidnight.set(totalHits);
        hits.save(new DailyHits(todayHits.get()));
    }

    public long getTodayHits() {
        return hits.totalHits() - hitsAtMidnight.get();
    }

    public long getTotalHits() {
        return this.hits.totalHits();
    }

    public long getYesterdayHits() {
        return this.yesterdayHits.get();
    }

    public List<DailyHits> getDailyHits() {
        return this.hits.getDailyHits();
    }

}
