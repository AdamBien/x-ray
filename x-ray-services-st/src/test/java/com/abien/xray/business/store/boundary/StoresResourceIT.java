/*
 */
package com.abien.xray.business.store.boundary;

import com.abien.xray.business.RESTSupport;
import javax.json.JsonArray;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author adam-bien.com
 */
public class StoresResourceIT {

    private static final String ROOT_TARGET = "http://localhost:8080/x-ray/resources/stores/";
    private WebTarget tut;

    @Before
    public void initClient() {
        Client client = ClientBuilder.newClient();
        this.tut = client.target(ROOT_TARGET);
    }

    @Test
    public void allStores() {
        JsonArray result = RESTSupport.convertToArrayFrom(this.tut.request(MediaType.TEXT_PLAIN).get(String.class));
        int nbrOfStores = result.size();
        Assert.assertThat(nbrOfStores, is(3));
    }

    @Test
    public void fetchUnknownStore() {
        Response response = this.tut.path("SHOULD-NOT-EXIST").
                request(MediaType.TEXT_PLAIN).
                get(Response.class);
        int status = response.getStatus();
        assertThat(status, is(204));
    }

}
