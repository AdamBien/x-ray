/*
 */
package com.airhacks.satellite.cache.boundary;

import com.hazelcast.core.IQueue;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;

/**
 *
 * @author adam-bien.com
 */
public class QueueGridResource implements GridResource {

    IQueue<String> cache;

    public QueueGridResource(IQueue<String> cache) {
        this.cache = cache;
    }

    @GET
    public JsonArray all(@DefaultValue("100") @QueryParam("max") int max) {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        cache.stream().limit(max).forEach(entry -> {
            builder.add(entry);
        });
        return builder.build();
    }

    @PUT
    public void put(JsonObject jsonObject) {
        jsonObject.entrySet().stream().forEach(e -> {
            cache.add(jsonObject.toString());
        });
    }

    @DELETE
    @Override
    public void delete(String key) {
        this.cache.remove(key);
    }
}
