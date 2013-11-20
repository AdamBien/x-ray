package com.abien.xray.business.maintenance.boundary;

import com.abien.xray.business.store.control.HitsManagement;
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

    @Inject
    HitsManagement hits;

    @Inject
    private String version;

    @GET
    public String version() {
        return this.version;
    }
}
