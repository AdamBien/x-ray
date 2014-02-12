package com.abien.xray.satellite.versioning.boundary;

import static com.abien.xray.satellite.RESTSupport.convertToObjectFrom;
import com.abien.xray.satellite.ServerLocation;
import javax.json.JsonObject;
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

    private static final String ROOT_TARGET = ServerLocation.getLocation() + "/satellite/resources/version/";
    private WebTarget tut;

    @Before
    public void initClient() {
        Client client = ClientBuilder.newClient();
        this.tut = client.target(ROOT_TARGET);
    }

    @Test
    public void fetchVersion() {
        String rawValue = this.tut.request().get(String.class);
        JsonObject json = convertToObjectFrom(rawValue);
        assertNotNull(json);
        JsonString value = json.getJsonString("version");
        assertNotNull(value);
        String versionNumber = value.getString();
        char majorVersion = versionNumber.charAt(0);
        Integer.parseInt(String.valueOf(majorVersion));
        assertTrue(versionNumber.contains("."));
    }
}
