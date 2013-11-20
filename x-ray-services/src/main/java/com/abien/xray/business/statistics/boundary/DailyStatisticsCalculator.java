package com.abien.xray.business.statistics.boundary;

import com.abien.xray.business.statistics.entity.DailyHits;
import com.abien.xray.business.store.control.HitsManagement;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.AccessTimeout;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Adam Bien, blog.adam-bien.com
 */
@Path("hitsperday")
@Singleton
@Startup
@AccessTimeout(2000)
public class DailyStatisticsCalculator {

    @Inject
    HitsManagement hits;

    private long todayHits = 0;
    private long yesterdayHits = 0;

    @PostConstruct
    public void restoreStatistics() {
        long totalHits = getTotalHits();
        long yesterdayHitsFromHistory = getYesterdayHitsFromHistory();
        this.yesterdayHits = totalHits;
        this.todayHits = yesterdayHitsFromHistory;

    }

    @Lock(LockType.WRITE)
    @Schedule(hour = "23", minute = "59", dayOfWeek = "*", dayOfMonth = "*", persistent = false)
    public void computeStatistics() {
        long totalHits = getTotalHits();
        todayHits = totalHits - yesterdayHits;
        yesterdayHits = totalHits;
        hits.save(new DailyHits(todayHits));
    }

    @GET
    @Path("yesterday")
    @Produces({"text/plain"})
    @Lock(LockType.READ)
    public String getYesterdaysHit() {
        return String.valueOf(todayHits);
    }

    @GET
    @Path("today")
    @Produces({"text/plain"})
    @Lock(LockType.READ)
    public String getTodaysHit() {
        return String.valueOf(getTotalHits() - yesterdayHits);
    }

    @GET
    @Path("history")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Lock(LockType.READ)
    @SuppressWarnings("")
    public List<DailyHits> getHistory() {
        return this.hits.getDailyHits();
    }

    long getYesterdayHitsFromHistory() {
        List<DailyHits> history = getHistory();
        if (history == null || history.isEmpty()) {
            return 0;
        } else {
            Collections.reverse(history);
            return history.get(0).getHit();
        }
    }

    long getTotalHits() {
        return hits.totalHits();
    }

}
