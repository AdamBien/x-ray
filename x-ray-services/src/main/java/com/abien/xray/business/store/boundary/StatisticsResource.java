package com.abien.xray.business.store.boundary;

import com.abien.xray.business.monitoring.PerformanceAuditor;
import com.abien.xray.business.store.control.PersistentHitStore;
import com.abien.xray.business.store.entity.Hit;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author abien
 */
@Path("statistics")
@Stateless
@Interceptors(PerformanceAuditor.class)
public class StatisticsResource {

    @EJB
    PersistentHitStore hits;

    @GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public List<Hit> totalHitsAsString(@QueryParam("exclude") String excludeContaining,@QueryParam("max") @DefaultValue("10") int max){
        if(excludeContaining == null){
            return hits.getMostPopularURLs(max);
        }else{
            return hits.getMostPopularURLs(excludeContaining,max);
        }
    }
}
