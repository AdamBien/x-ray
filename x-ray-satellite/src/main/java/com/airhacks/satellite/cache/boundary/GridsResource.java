/*
 */
package com.airhacks.satellite.cache.boundary;

import com.airhacks.xray.grid.control.Grid;
import com.hazelcast.core.IMap;
import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author adam-bien.com
 */
@Path("grids")
@Stateless
public class GridsResource {

    @Inject
    @Any
    Instance<IMap<String, String>> caches;

    @GET
    public JsonArray grids() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        Grid.Name[] values = Grid.Name.values();
        for (Grid.Name name : values) {
            arrayBuilder.add(name.name().toLowerCase());
        }
        return arrayBuilder.build();
    }

}
