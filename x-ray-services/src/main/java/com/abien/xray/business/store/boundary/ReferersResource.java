package com.abien.xray.business.store.boundary;

import com.abien.xray.business.monitoring.PerformanceAuditor;
import com.abien.xray.business.store.control.HitsManagement;
import com.abien.xray.business.store.entity.CacheValue;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
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
@Path("referers")
@Stateless
@Interceptors(PerformanceAuditor.class)
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
