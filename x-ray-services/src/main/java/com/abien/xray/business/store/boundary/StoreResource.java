/*
 */
package com.abien.xray.business.store.boundary;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;

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

}
