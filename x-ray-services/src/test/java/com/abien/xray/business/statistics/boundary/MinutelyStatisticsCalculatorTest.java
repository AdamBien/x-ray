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
public class MinutelyStatisticsCalculatorTest {

    @Test
    public void computeStatisticsSchedule() {
        Duration duration = Duration.parse("PT" + MinutelyStatisticsCalculator.DURATION);
        assertThat(duration.getSeconds(), is(60l));
    }

}
