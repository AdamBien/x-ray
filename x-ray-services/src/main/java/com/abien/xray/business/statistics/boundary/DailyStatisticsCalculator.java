package com.abien.xray.business.statistics.boundary;

import com.abien.xray.business.statistics.entity.DailyHits;
import com.abien.xray.business.store.boundary.Hits;

import java.util.*;
import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
@DependsOn("Hits")
@AccessTimeout(2000)
public class DailyStatisticsCalculator {

    @EJB
    Hits hits;

    private long todayHits = 0;
    private long yesterdayHits = 0;

    @PersistenceContext
    EntityManager em;


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
        em.persist(new DailyHits(todayHits));
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
        return this.em.createNamedQuery(DailyHits.findAllDescending).getResultList();
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
