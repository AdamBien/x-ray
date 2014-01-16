/*
 */
package com.abien.xray.business.grid.boundary;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author adam-bien.com
 */
public class StoreResource {

    private final Map<String, AtomicLong> cache;

    public StoreResource(Map<String, AtomicLong> cache) {
        this.cache = cache;
    }

    @GET
    public JsonObject all() {
        Set<Map.Entry<String, AtomicLong>> entrySet = this.cache.entrySet();
        JsonObjectBuilder builder = Json.createObjectBuilder();
        entrySet.forEach(t -> {
            builder.add(t.getKey(), t.getValue().get());
        });
        return builder.build();
    }

    @PUT
    public Response put(JsonObject entry) {
        Set<Map.Entry<String, JsonValue>> entrySet = entry.entrySet();
        entrySet.forEach(e -> {
            cache.put(e.getKey(), convert(e.getValue()));
        });
        return Response.ok().build();
    }

    @DELETE
    @Path("{entry}")
    public Response remove(@PathParam("entry") String entry) {
        AtomicLong removed = this.cache.remove(entry);
        return Response.ok().build();
    }

    static AtomicLong convert(JsonValue value) {
        return new AtomicLong(Long.parseLong(value.toString()));
    }

}
