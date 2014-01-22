/*
 */
package com.abien.xray.business.monitoring.boundary;

import java.util.Iterator;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author adam-bien.com
 */
public class HealthMonitorTest {

    HealthMonitor cut;

    @Before
    public void inject() {
        this.cut = new HealthMonitor();
        this.cut.methods = new MockedIMap();
    }

    @Test
    public void getSlowestMethodsWithEmptyMap() {
        List<String> slowestMethods = this.cut.getSlowestMethods();
        assertTrue(slowestMethods.isEmpty());
    }

    @Test
    public void getSlowestMethods() {

        this.cut.methods.put("slower", "2");
        this.cut.methods.put("very slow", "42");
        this.cut.methods.put("slow", "1");

        List<String> slowestMethods = this.cut.getSlowestMethods();
        assertThat(slowestMethods.size(), is(3));
        Iterator<String> iterator = slowestMethods.iterator();
        assertTrue(iterator.next().startsWith("very slow"));
        assertTrue(iterator.next().startsWith("slower"));
        assertTrue(iterator.next().startsWith("slow"));
    }

}
