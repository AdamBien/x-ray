/*
 */
package com.airhacks.satellite.cache.boundary;

import com.hazelcast.monitor.LocalMapStats;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 *
 * @author adam-bien.com
 */
@Path("caches-info")
@Stateless
public class CacheMetaDataResource {

    @Inject
    CacheExposer ce;

    @GET
    @Path("{name}")
    public String info(@PathParam("name") String cacheName) {
        LocalMapStats stats = ce.getStatistics(cacheName);
        return stats.toString();
    }

}
