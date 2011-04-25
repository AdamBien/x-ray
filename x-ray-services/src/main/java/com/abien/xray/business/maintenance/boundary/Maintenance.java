package com.abien.xray.business.maintenance.boundary;

import com.abien.xray.business.store.boundary.Hits;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Adam Bien <blog.adam-bien.com>
 */
@Stateless
@Path("maintenance")
@Produces(MediaType.TEXT_PLAIN)
public class Maintenance {

    @EJB
    Hits hits;

    @Inject
    private String version;

    @GET
    public String version() {
        return this.version;
    }


    @GET
    @Path("flushHits")
    public String flushHits() {
        long start = System.currentTimeMillis();
        hits.persistHitsCache();
        return "Hit flushed in: " + (System.currentTimeMillis() - start);
    }

    @GET
    @Path("flushReferers")
    public String flushReferersCache() {
        long start = System.currentTimeMillis();
        hits.persistReferersCache();
        return "Referers flushed in: " + (System.currentTimeMillis() - start);
    }

}
