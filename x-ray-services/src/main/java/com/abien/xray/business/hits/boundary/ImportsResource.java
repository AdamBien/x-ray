
package com.abien.xray.business.hits.boundary;

import com.abien.xray.business.hits.control.HitsCache;
import com.abien.xray.business.monitoring.PerformanceAuditor;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.json.JsonObject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

/**
 *
 * @author airhacks.com
 */
@Stateless
@Path("imports")
@Interceptors(PerformanceAuditor.class)
public class ImportsResource {

    @Inject
    HitsCache management;

    @PUT
    @Path("hits")
    public void update(JsonObject input) {
        String id = input.getString("id");
        long hits = input.getJsonNumber("hits").longValue();
        this.management.updateHitsForURI(id, String.valueOf(hits));
    }

}
