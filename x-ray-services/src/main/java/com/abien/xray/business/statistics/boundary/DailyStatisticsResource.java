package com.abien.xray.business.statistics.boundary;

import com.abien.xray.business.statistics.control.DailyHitsCalculator;
import com.abien.xray.business.statistics.entity.DailyHits;
import java.util.List;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
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
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class DailyStatisticsResource {

    @Inject
    DailyHitsCalculator calculator;

    @GET
    @Path("yesterday")
    @Produces({"text/plain"})
    public String getYesterdaysHit() {
        return String.valueOf(calculator.getYesterdayHits());
    }

    @GET
    @Path("today")
    @Produces({"text/plain"})
    public String getTodaysHit() {
        return String.valueOf(calculator.getTodayHits());
    }

    @GET
    @Path("history")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @SuppressWarnings("")
    public List<DailyHits> getHistory() {
        return this.calculator.getDailyHits();
    }

}
