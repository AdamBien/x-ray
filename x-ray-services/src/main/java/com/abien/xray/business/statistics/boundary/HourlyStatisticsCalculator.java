package com.abien.xray.business.statistics.boundary;

import com.abien.xray.business.HitsPer;
import com.abien.xray.business.store.boundary.Hits;

import static com.abien.xray.business.HitsPer.Frequency.*;

import javax.ejb.AccessTimeout;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author Adam Bien, blog.adam-bien.com
 */
@Path("hitsperhour")
@Singleton
@AccessTimeout(2000)
@Produces({"text/plain"})
public class HourlyStatisticsCalculator {

    @EJB
    Hits hits;

    private long currentRate = 0;
    private long maxRate = 0;
    private long lastMeasurement = 0;

    @Inject
    @HitsPer(HOUR)
    Event<Long> hourlyEvent;

    @Schedule(hour = "*/1", persistent = false)
    public void computeStatistics() {
        long totalHits = hits.totalHits();
        currentRate = totalHits - lastMeasurement;
        lastMeasurement = totalHits;
        maxRate = Math.max(maxRate, currentRate);
        hourlyEvent.fire(currentRate);
    }

    @GET
    public String getHitsPerHour() {
        return String.valueOf(currentRate);
    }

    @GET
    @Path("max")
    public String getMaxHitsPerHour() {
        return String.valueOf(maxRate);
    }

}
