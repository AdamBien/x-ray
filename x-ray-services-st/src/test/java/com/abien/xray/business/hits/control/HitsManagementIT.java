package com.abien.xray.business.hits.control;

import static org.junit.Assert.assertThat;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;
import com.abien.xray.business.ServerLocation;

import org.junit.Before;
import org.junit.Test;

/**
 * HitsManagementIT
 */
public class HitsManagementIT {

    private static final String ROOT_TARGET = ServerLocation.getLocation() + "/x-ray/resources/hits";
    private WebTarget tut;

    @Before
    public void initClient() {
        Client client = ClientBuilder.newClient();
        this.tut = client.target(ROOT_TARGET);
    }

    @Test
    public void getNotExistingURI() throws UnsupportedEncodingException {
        String numberOfHits = this.tut.
        path(URLEncoder.encode("/entry/NOTEXISTS","UTF-8")).
                request(MediaType.TEXT_PLAIN).
        get(String.class);
        assertThat(numberOfHits, is("0"));
    }

    @Test
    public void putAndCount() throws UnsupportedEncodingException {
        long id = System.nanoTime();
        String rawURL = "/entry/NEW" + id;
        String uri = URLEncoder.encode(rawURL,"UTF-8");

        String numberOfHits = this.tut.path(uri).request(MediaType.TEXT_PLAIN).get(String.class);
        assertThat(numberOfHits, is("0"));

        Response response = this.tut.request(MediaType.TEXT_PLAIN).put(Entity.text(rawURL));
        assertThat(response.getStatus(), is(204));

        numberOfHits = this.tut.path(uri).request(MediaType.TEXT_PLAIN).get(String.class);
        assertThat(numberOfHits, is("1"));

    }

    
}