/*
 */
package com.abien.xray.business.hits.control;

import java.time.Duration;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class HourlyTrendResetTest {

    @Test
    public void hourlyReset() {
        Duration duration = Duration.parse("PT" + HourlyTrendReset.DURATION);
        assertNotNull(duration);
        assertThat(duration.getSeconds(), is(3600l));
    }

}
