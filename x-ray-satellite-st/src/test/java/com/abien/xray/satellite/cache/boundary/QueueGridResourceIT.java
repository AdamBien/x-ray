/*
 */
package com.abien.xray.satellite.cache.boundary;

import com.abien.xray.satellite.RESTSupport;
import com.abien.xray.satellite.ServerLocation;
import java.io.UnsupportedEncodingException;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author adam-bien.com
 */
public class QueueGridResourceIT {

    private static final String ROOT_TARGET = ServerLocation.getLocation() + "/satellite/resources/grids/";
    private WebTarget tut;
    private static final int NBR_OF_ALL_GRIDS = 8;
    private static final String A_QUEUE_STORE = "FIREHOSE";
    private static final String QUEUE_CACHE_TYPE = "queues";

    @Before
    public void initClient() {
        Client client = ClientBuilder.newClient();
        this.tut = client.target(ROOT_TARGET);
    }

    @Test
    public void fetchUnknownStore() {
        Response response = this.tut.path(QUEUE_CACHE_TYPE).path("SHOULD-NOT-EXIST").
                request(MediaType.TEXT_PLAIN).
                get(Response.class);
        int status = response.getStatus();
        assertThat(status, is(400));
    }

    @Test
    public void put() {
        final String content = "content";
        final String expected = "the answer";
        JsonObjectBuilder builder = Json.
                createObjectBuilder().
                add(content, expected);
        Response response = put(builder);
        assertThat(response.getStatus(), is(204));
    }

    @Test
    public void all() throws UnsupportedEncodingException {
        this.put();
        String rawContent = this.tut.
                path(QUEUE_CACHE_TYPE).
                path(A_QUEUE_STORE).
                request(MediaType.TEXT_PLAIN).
                get(String.class);
        JsonArray array = RESTSupport.convertToArrayFrom(rawContent);
        assertNotNull(array);

    }

    @Test
    public void size() {
        String size = this.tut.
                path(QUEUE_CACHE_TYPE).
                path(A_QUEUE_STORE).
                path("size").
                request(MediaType.TEXT_PLAIN).
                get(String.class);
        assertNotNull(size);
        int sizeNbr = Integer.parseInt(size);
        assertTrue(sizeNbr >= 0);
    }

    @Test
    public void delete() {
    }

    Response put(JsonObjectBuilder builder) {
        Response response = this.tut.
                path(QUEUE_CACHE_TYPE).
                path(A_QUEUE_STORE).
                request(MediaType.TEXT_PLAIN).
                put(Entity.json(builder.build()));
        return response;
    }

}
