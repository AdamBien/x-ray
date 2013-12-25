/*
 */
package com.abien.xray.probe.http;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;

/**
 *
 * @author adam-bien.com
 */
public class HTTPRequestRESTInterceptorTest {

    HTTPRequestRESTInterceptor cut;

    @Before
    public void initialize() {
        this.cut = new HTTPRequestRESTInterceptor();
    }

    @Test
    public void systemPropertiesAreOverwritingTheDefault() throws ServletException {
        final String expected = "http://airhacks.com";
        System.setProperty(HTTPRequestRESTInterceptor.XRAYURL, expected);
        this.cut.init(mock(FilterConfig.class));
        assertThat(this.cut.serviceURL, is(expected));
    }

}
