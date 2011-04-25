package com.abien.xray.xrayfitnesse;

import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author abien
 */
@Ignore
public class HitsFixtureTest {

    HitsFixture fixture;

    @Before
    public void initialize() throws MalformedURLException{
        this.fixture = new HitsFixture();
        this.fixture.initializeCounter();
    }

    @Test
    public void sendUrl() {
        String uri = "/entry/something|localhost";
        this.fixture.sendURL(uri);
        assertTrue(this.fixture.totalHitsAre("1"));
        //assertTrue(this.fixture.todayHitsAre("1"));
        uri = "/entry/another|localhost";
        this.fixture.sendURL(uri);
        assertTrue(this.fixture.totalHitsAre("2"));

    }

}