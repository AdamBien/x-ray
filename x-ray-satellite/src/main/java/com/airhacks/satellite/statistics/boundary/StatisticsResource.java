package com.airhacks.satellite.statistics.boundary;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * @author airhacks.com
 */
@Stateless
@Path("statistics")
public class StatisticsResource {

    @Inject
    StatisticsProvider sp;

    @GET
    @Path("maps")
    public JsonArray mapStatistics() {
        return sp.mapStatistics();
    }

    @GET
    @Path("queues")
    public JsonArray queueStatistics() {
        return sp.queueStatistics();
    }

}
