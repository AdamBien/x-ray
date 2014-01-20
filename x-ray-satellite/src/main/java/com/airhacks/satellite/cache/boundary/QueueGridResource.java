/*
 */
package com.airhacks.satellite.cache.boundary;

import com.airhacks.satellite.cache.control.Serializer;
import com.hazelcast.core.IQueue;
import java.io.IOException;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 *
 * @author adam-bien.com
 */
public class QueueGridResource {

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

    @GET
    @Path("size")
    public String size() {
        return String.valueOf(this.cache.size());
    }

    @PUT
    public void put(JsonObject jsonObject) throws IOException {
        cache.add(Serializer.serialize(jsonObject));
    }

    @DELETE
    @Path("{index}")
    public void delete(JsonObject object) throws IOException {
        this.cache.remove(Serializer.serialize(object));
    }
}
