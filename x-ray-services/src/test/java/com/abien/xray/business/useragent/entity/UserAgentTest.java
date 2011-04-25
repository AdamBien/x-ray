package com.abien.xray.business.useragent.entity;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * User: blog.adam-bien.com
 * Date: 07.01.11
 * Time: 17:09
 */
public class UserAgentTest {

    @Test
    public void normalizeKnownBrowserMozilla(){
       String mozilla = "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)";
       UserAgent userAgent = new UserAgent();
        String expected = "FIREFOX";
        String actual = userAgent.normalize(mozilla);
        assertThat(actual,is(expected));
    }
    @Test
    public void normalizeKnownBrowserMSIE(){
       String msie = "HuaweiSymantecSpider/1.0+DSE-support@huaweisymantec.com+(compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR ";
       UserAgent userAgent = new UserAgent();
        String expected = "MSIE";
        String actual = userAgent.normalize(msie);
        assertThat(actual,is(expected));
    }
}
