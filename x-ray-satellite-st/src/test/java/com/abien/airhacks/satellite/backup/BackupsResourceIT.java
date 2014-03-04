package com.abien.airhacks.satellite.backup;

import com.abien.airhacks.satellite.ServerLocation;
import com.abien.airhacks.satellite.cache.boundary.MapGridResourceIT;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * @author airhacks.com
 */
public class BackupsResourceIT {

    private static final String ROOT_TARGET = ServerLocation.getLocation() + "/satellite/resources/backups/{name}";
    private WebTarget tut;
    private static final String STORE_NAME = "hits";

    @Before
    public void initClient() {
        Client client = ClientBuilder.newClient();
        this.tut = client.target(ROOT_TARGET).resolveTemplate("name", STORE_NAME);
    }

    @Test
    public void download() throws UnsupportedEncodingException {
        MapGridResourceIT putClient = new MapGridResourceIT();
        putClient.initClient();
        putClient.put();
        Response response = this.tut.request().accept(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), is(200));
        String value = response.getHeaderString("x-number-of-objects");
        Assert.assertNotNull(value);
        int number = Integer.parseInt(value);
        assertTrue(number >= 0);
        JsonObject backupObject = response.readEntity(JsonObject.class);
        assertNotNull(backupObject);
        JsonObject storeBackup = backupObject.getJsonObject(STORE_NAME);
        assertNotNull(storeBackup);
    }

    @Test
    public void upload() {
        String backupData = "{\"hits\":{\"42\":\"{\\\"content\\\":\\\"the answer\\\"}\"}}";
        JsonReader reader = Json.createReader(new StringReader(backupData));
        JsonObject backup = reader.readObject();
        Response response = this.tut.request().put(Entity.json(backup));
        assertThat(response.getStatus(), is(200));
    }
}
