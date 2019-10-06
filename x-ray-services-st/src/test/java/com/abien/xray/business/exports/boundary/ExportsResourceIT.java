package com.abien.xray.business.exports.boundary;

import static org.junit.Assert.assertNotNull;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.abien.xray.business.ServerLocation;

import org.junit.Before;
import org.junit.Test;

/**
 * ExportsResourceIT
 */
public class ExportsResourceIT {

    private static final String ROOT_TARGET = ServerLocation.getLocation() + "/x-ray/resources/exports";
    private WebTarget tut;

    @Before
    public void initClient() {
        Client client = ClientBuilder.newClient();
        this.tut = client.target(ROOT_TARGET);
    }

    @Test
    public void export() {
        JsonArray export = this.tut.request(MediaType.APPLICATION_JSON).get(JsonArray.class);
        assertNotNull(export);
        System.out.println("-- " + export);
    }

}