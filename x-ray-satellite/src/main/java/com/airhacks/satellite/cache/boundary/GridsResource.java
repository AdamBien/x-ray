/*
 */
package com.airhacks.satellite.cache.boundary;

import com.airhacks.xray.grid.control.Grid;
import com.airhacks.xray.grid.control.GridInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.IQueue;
import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 *
 * @author adam-bien.com
 */
@Path("grids")
@Stateless
public class GridsResource {

    @Inject
    @Any
    Instance<IMap<String, String>> mapCaches;

    @Inject
    @Any
    Instance<IQueue<String>> queueCaches;

    @GET
    public JsonArray grids() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        Grid.Name[] values = Grid.Name.values();
        for (Grid.Name name : values) {
            arrayBuilder.add(name.name().toLowerCase());
        }
        return arrayBuilder.build();
    }

    @Path("/maps/{id}")
    public MapGridResource mapGrid(@PathParam("id") @GridName String name) {
        final GridInstance annotation = new GridInstance(name);
        final Instance<IMap<String, String>> maps = mapCaches.select(annotation);
        boolean mapUnsatisfied = maps.isUnsatisfied();
        if (!mapUnsatisfied) {
            IMap<String, String> cache = maps.get();
            return new MapGridResource(cache);
        }
        return null;
    }

    @Path("/queues/{id}")
    public QueueGridResource queueGrid(@PathParam("id") @GridName String name) {
        final GridInstance annotation = new GridInstance(name);
        boolean queueUnsatisfied = queueCaches.select(annotation).isUnsatisfied();
        if (!queueUnsatisfied) {
            IQueue<String> cache = queueCaches.select(annotation).get();
            return new QueueGridResource(cache);
        }
        return null;
    }

}
