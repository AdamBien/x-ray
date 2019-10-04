/*
 */
package com.abien.xray.business.statistics.boundary;

import java.time.Duration;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class HourlyStatisticsCalculatorTest {

    @Test
    public void hourlyScheduler() {
        Duration duration = Duration.parse("PT" + HourlyStatisticsCalculator.DURATION);
        assertThat(duration.getSeconds(), is(3600l));
    }

}
