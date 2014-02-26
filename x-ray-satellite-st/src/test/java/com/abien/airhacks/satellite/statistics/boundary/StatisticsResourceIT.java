/*
 */
package com.abien.airhacks.satellite.statistics.boundary;

import com.abien.airhacks.satellite.RESTSupport;
import com.abien.airhacks.satellite.ServerLocation;
import javax.json.JsonArray;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author adam-bien.com
 */
public class StatisticsResourceIT {

    private static final String ROOT_TARGET = ServerLocation.getLocation() + "/satellite/resources/statistics/";
    private WebTarget tut;

    @Before
    public void initClient() {
        Client client = ClientBuilder.newClient();
        this.tut = client.target(ROOT_TARGET);
    }

    @Test
    public void mapStatistics() {
        final String rawResult = this.tut.path("maps").request(MediaType.TEXT_PLAIN).get(String.class);
        JsonArray result = RESTSupport.convertToArrayFrom(rawResult);
        assertFalse(result.isEmpty());
    }

    @Test
    public void queueStatistics() {
        final String rawResult = this.tut.path("queues").request(MediaType.TEXT_PLAIN).get(String.class);
        JsonArray result = RESTSupport.convertToArrayFrom(rawResult);
        assertFalse(result.isEmpty());
    }

}
