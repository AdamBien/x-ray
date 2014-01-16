package com.abien.xray.business.statistics.boundary;

import com.abien.xray.business.hits.control.HitsManagement;
import com.abien.xray.business.statistics.entity.DailyHits;
import com.hazelcast.core.IAtomicLong;
import java.util.List;
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

    @Inject
    private IAtomicLong todayHits;
    @Inject
    private IAtomicLong yesterdayHits;

    @Lock(LockType.WRITE)
    @Schedule(hour = "23", minute = "59", dayOfWeek = "*", dayOfMonth = "*", persistent = false)
    public void computeStatistics() {
        long totalHits = getTotalHits();
        todayHits.set(totalHits - yesterdayHits.get());
        yesterdayHits.set(totalHits);
        hits.save(new DailyHits(todayHits.get()));
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
        return String.valueOf(getTotalHits() - yesterdayHits.get());
    }

    @GET
    @Path("history")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Lock(LockType.READ)
    @SuppressWarnings("")
    public List<DailyHits> getHistory() {
        return this.hits.getDailyHits();
    }

    long getTotalHits() {
        return hits.totalHits();
    }

}
