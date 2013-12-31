package com.abien.xray.business.versioning.boundary;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/*
 */
/**
 *
 * @author adam-bien.com
 */
public class VersionResourceIT {

    private static final String ROOT_TARGET = "http://localhost:8080/x-ray/resources/version/";
    private WebTarget tut;

    @Before
    public void initClient() {
        Client client = ClientBuilder.newClient();
        this.tut = client.target(ROOT_TARGET);
    }

    @Test
    public void fetchVersion() {
        String rawValue = this.tut.request().get(String.class);
        JsonObject json = convertFrom(rawValue);
        assertNotNull(json);
        JsonString value = json.getJsonString("version");
        assertNotNull(value);
        String versionNumber = value.getString();
        char majorVersion = versionNumber.charAt(0);
        Integer.parseInt(String.valueOf(majorVersion));
        assertTrue(versionNumber.contains("."));
    }

    JsonObject convertFrom(String rawValue) {
        JsonReader reader = Json.createReader(new StringReader(rawValue));
        return reader.readObject();
    }

}
