package com.abien.xray.business.statistics.boundary;

import com.abien.xray.business.HitsPer;
import com.abien.xray.business.hits.control.HitsManagement;

import static com.abien.xray.business.HitsPer.Frequency.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author Adam Bien, blog.adam-bien.com
 */
@Path("hitsperhour")
@ApplicationScoped
@Produces({"text/plain"})
public class HourlyStatisticsCalculator {

    @Inject
    HitsManagement hits;

    private long currentRate = 0;
    private long maxRate = 0;
    private long lastMeasurement = 0;

    @Inject
    @HitsPer(HOUR)
    Event<Long> hourlyEvent;

    //@Schedule(hour = "*/1", persistent = false)
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
