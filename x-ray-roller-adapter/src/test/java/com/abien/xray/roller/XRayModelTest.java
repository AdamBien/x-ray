/*
 *//*
 */
package com.abien.xray.roller;

import com.abien.xray.XRay;
import java.util.HashMap;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author adam-bien.com
 */
public class XRayModelTest {

    private XRayModel cut;

    @Before
    public void init() {
        this.cut = new XRayModel();
    }

    @Test
    public void initWithSystemProperties() {
        final String expected = "system-uri";
        System.setProperty(XRayModel.XRAYURL, expected);
        this.cut.init(new HashMap());
        XRay xray = this.cut.getXray();
        String actual = xray.getBaseUrl();
        assertThat(actual, is(expected));
    }

    @Test
    public void initWithNullMap() {
        cut.init(null);
        XRay actual = cut.getXray();
        Assert.assertNotNull(actual);
        String baseUrl = actual.getBaseUrl();
        Assert.assertNotNull(baseUrl);
    }
}
