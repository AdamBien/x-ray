/*
 */
package com.abien.xray.scenarios;

import com.abien.xray.business.ServerLocation;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author adam-bien.com
 */
public class UpdateURIIT {

    public final static String HEADER_PREFIX = "xray_";
    private static final String ROOT_TARGET = ServerLocation.getLocation() + "/x-ray-services/resources/hits/";
    private WebTarget tut;
    private final static long WAIT_TIME = 8 * 1000;

    @Before
    public void initClient() {
        Client client = ClientBuilder.newClient();
        this.tut = client.target(ROOT_TARGET);
    }

    @Test
    public void putNewValidUri() throws UnsupportedEncodingException, InterruptedException {
        String userAgentHeader = HEADER_PREFIX + HttpHeaders.USER_AGENT.toLowerCase();

        String uri = "/entry/news" + System.currentTimeMillis();
        Response response = this.tut.request().header(HEADER_PREFIX + "referer", "twitter").header(userAgentHeader, "mozilla").put(Entity.text(uri));
        assertThat(response.getStatus(), is(204));
        String encoded = URLEncoder.encode(uri, "UTF-8");
        Thread.sleep(WAIT_TIME);
        String numberOfHits = this.tut.path(encoded).request().get(String.class);
        assertNotNull(numberOfHits);
        long nbr = Long.parseLong(numberOfHits);
        assertThat(nbr, is(1l));
    }

}
