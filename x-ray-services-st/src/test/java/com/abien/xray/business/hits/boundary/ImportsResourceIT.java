package com.abien.xray.business.hits.boundary;

import com.abien.xray.business.ServerLocation;
import com.abien.xray.business.hits.control.HitsManagementIT;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import static javax.ws.rs.client.Entity.json;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

public class ImportsResourceIT {

    private static final String ROOT_TARGET = ServerLocation.getLocation() + "/x-ray/resources/imports/hits";
    private WebTarget tut;

    @Before
    public void initClient() {
        Client client = ClientBuilder.newClient();
        this.tut = client.target(ROOT_TARGET);
    }
    
    @Test
    public void importHitAndIncreaseCounter() throws UnsupportedEncodingException {
        String articleName = "popular" + System.currentTimeMillis();
        JsonObject article = Json.createObjectBuilder().
                add("id", articleName).
                add("hits", "12").
                build();
        Response response = this.tut.request(MediaType.TEXT_PLAIN).
                put(json(article));
        Integer hit = response.readEntity(Integer.class);
        assertThat(hit, is(12));

        response = this.tut.request(MediaType.TEXT_PLAIN).
                put(json(article));
        hit = response.readEntity(Integer.class);
        assertThat(hit, is(12 * 2));

        hit = numberOfHits(articleName);
        assertThat(hit, is(12 * 2));

    }

    public int numberOfHits(String uri) throws UnsupportedEncodingException {
        HitsManagementIT hits = new HitsManagementIT();
        hits.initClient();
        String encoded = URLEncoder.encode("/entry/" + uri, "UTF-8");
        return Integer.parseInt(hits.numberOfHits(encoded));
    }
    
}