/*
 */
package com.abien.xray.business.statistics.control;

import com.abien.xray.business.grid.control.Grid;
import com.abien.xray.business.hits.control.HitsManagement;
import com.abien.xray.business.logging.boundary.XRayLogger;
import com.abien.xray.business.statistics.entity.DailyHits;
import io.quarkus.scheduler.Scheduled;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author adam-bien.com
 */
@ApplicationScoped
public class DailyHitsCalculator {

    @Inject
    HitsManagement hits;

    @Inject
    XRayLogger LOG;

    @Inject
    @Grid(Grid.Name.DAILY)
    Map<String, String> dailyHistory;

    AtomicLong totalHitsAtMidnight;
    AtomicLong yesterdayHits;

    @PostConstruct
    public void initializeYesterday() {
        this.totalHitsAtMidnight = new AtomicLong();
        this.yesterdayHits = new AtomicLong(getYesterdayHitsFromHistory());
        LOG.log(Level.INFO, "Initializing DailyStatisticsCalculator");
        if (this.yesterdayHits.get() == 0) {
            LOG.log(Level.INFO, "Yesterday's hits are 0");
            this.totalHitsAtMidnight.set(hits.totalHits());
        } else {
            this.totalHitsAtMidnight.set(getTotalHits() - this.yesterdayHits.get());
        }
    }

    @Scheduled(cron = "23 59 * * *")
    public void computeDailyHits() {
        AtomicLong todayHits = new AtomicLong(0);
        LOG.log(Level.INFO, "Computing daily hits");
        long totalHits = hits.totalHits();
        LOG.log(Level.INFO, "Total hits: " + totalHits);
        LOG.log(Level.INFO, "Yesterday's hits were: " + totalHitsAtMidnight.get());
        todayHits.set(totalHits - totalHitsAtMidnight.get());
        yesterdayHits.set(todayHits.get());
        LOG.log(Level.INFO, "Today hits: " + todayHits.get());
        totalHitsAtMidnight.set(totalHits);
        hits.save(new DailyHits(todayHits.get()));
    }

    public long getTodayHits() {
        return hits.totalHits() - totalHitsAtMidnight.get();
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

    public LocalDate getYesterdayDate() {
        return this.dailyHistory.entrySet().
                stream().
                map(h -> LocalDate.parse(h.getKey())).
                sorted().
                findFirst().
                orElse(null);
    }

    long getYesterdayHitsFromHistory() {
        LocalDate yesterdayDate = getYesterdayDate();
        if (yesterdayDate == null) {
            return 0;
        }
        String dateAsString = yesterdayDate.format(DateTimeFormatter.ISO_DATE);
        String yesterdayHit = this.dailyHistory.get(dateAsString);
        if (yesterdayHit == null) {
            return 0;
        } else {
            return Long.parseLong(yesterdayHit);
        }
    }
}
