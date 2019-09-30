package com.abien.xray.business.hits.boundary;

import com.abien.xray.business.hits.control.HitsManagement;
import com.abien.xray.business.hits.entity.CacheValue;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
@RequestScoped
@Path("referers")
public class ReferersResource {

    @Inject
    HitsManagement hits;

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<CacheValue> topReferers(@QueryParam("exclude") String excludeContaining,
            @QueryParam("max") @DefaultValue("50") int maxNumber) {
        if (excludeContaining == null) {
            return hits.topReferers(maxNumber);
        } else {
            return hits.topReferers(excludeContaining, maxNumber);
        }
    }
}
