/*
 */
package com.abien.xray.business.grid.boundary;

import com.abien.xray.business.grid.control.Grid;
import com.abien.xray.business.grid.control.GridInstance;
import com.abien.xray.business.hits.control.HitsCache;
import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.validation.constraints.Size;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("stores")
@Stateless
public class StoresResource {

    @Inject
    @Any
    Instance<HitsCache> caches;

    @Context
    ResourceContext rc;

    @Path("{id}")
    public StoreResource resource(@Size(min = 3) @PathParam("id") String name) {
        GridInstance annotation = null;
        try {
            annotation = new GridInstance(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new WebApplicationException(Response.Status.NO_CONTENT);
        }
        Instance<HitsCache> cacheInstance = caches.select(annotation);
        if (cacheInstance.isUnsatisfied()) {
            return null;
        }
        final HitsCache hitCache = cacheInstance.get();
        return rc.initResource(new StoreResource(hitCache.getCache()));
    }

    @GET
    public JsonArray all() {
        Grid.Name[] values = Grid.Name.values();
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (Grid.Name name : values) {
            builder.add(name.name().toLowerCase());
        }
        return builder.build();
    }
}
