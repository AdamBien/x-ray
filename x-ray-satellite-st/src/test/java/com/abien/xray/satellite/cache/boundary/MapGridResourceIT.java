/*
 */
package com.abien.xray.satellite.cache.boundary;

import com.abien.xray.satellite.RESTSupport;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author adam-bien.com
 */
public class MapGridResourceIT {

    private static final String ROOT_TARGET = "http://localhost:8080/satellite/resources/grids/";
    private WebTarget tut;
    private static final int NBR_OF_ALL_GRIDS = 8;
    private static final String MAP_STORE = "hits";
    private static final String MAP_CACHE_TYPE = "maps";

    @Before
    public void initClient() {
        Client client = ClientBuilder.newClient();
        this.tut = client.target(ROOT_TARGET);
    }

    @Test
    public void allGrids() {
        JsonArray result = RESTSupport.convertToArrayFrom(this.tut.request(MediaType.TEXT_PLAIN).get(String.class));
        int nbrOfStores = result.size();
        Assert.assertThat(nbrOfStores, is(NBR_OF_ALL_GRIDS));
    }

    @Test
    public void fetchUnknownStore() {
        Response response = this.tut.path(MAP_CACHE_TYPE).path("SHOULD-NOT-EXIST").
                request(MediaType.TEXT_PLAIN).
                get(Response.class);
        int status = response.getStatus();
        assertThat(status, is(400));
    }

    @Test
    public void put() throws UnsupportedEncodingException {
        final String key = "42";
        final String content = "content";
        final String expected = "the answer";
        JsonObjectBuilder builder = Json.
                createObjectBuilder().
                add(content, expected);
        Response response = put(key, builder);
        assertThat(response.getStatus(), is(204));

        JsonObject value = getValueForKey(key, MAP_STORE);
        assertNotNull(value);
        System.out.println("JsonObject: " + value);
        String actual = value.getString(content);
        assertThat(actual, is(expected));
    }

    Response put(String rawKey, JsonObjectBuilder builder) throws UnsupportedEncodingException {
        String key = URLEncoder.encode(rawKey, "UTF-8");
        Response response = this.tut.
                path(MAP_CACHE_TYPE).
                path(MAP_STORE).
                path(key).
                request(MediaType.TEXT_PLAIN).
                put(Entity.json(builder.build()));
        return response;
    }

    @Test
    public void delete() throws UnsupportedEncodingException {
        final String key = URLEncoder.encode("-" + System.currentTimeMillis(), "UTF-8");
        String expected = "duke";
        JsonObjectBuilder builder = Json.createObjectBuilder().add(key, expected);
        put(key, builder);

        JsonObject entry = getValueForKey(key, MAP_STORE);
        JsonValue value = entry.get(key);
        assertNotNull(value);

        Response response = this.tut.path(MAP_CACHE_TYPE).path(MAP_STORE).path(key).request().delete();
        assertThat(response.getStatus(), is(204));
        entry = getValueForKey(key, MAP_STORE);
        assertNull(entry);
    }

    JsonObject getValueForKey(String raw, String storeName) throws UnsupportedEncodingException {
        String key = URLEncoder.encode(raw, "UTF-8");
        String rawContent = this.tut.
                path(MAP_CACHE_TYPE).
                path(storeName).
                path(key).
                request(MediaType.TEXT_PLAIN).
                get(String.class);
        if (rawContent == null || rawContent.isEmpty()) {
            return null;
        }
        return RESTSupport.convertToObjectFrom(rawContent);
    }

    private JsonObject all(String storeName) {
        String rawContent = this.tut.path(storeName).
                request(MediaType.TEXT_PLAIN).
                get(String.class);
        return RESTSupport.convertToObjectFrom(rawContent);
    }
}
