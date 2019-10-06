package com.abien.xray.business.statistics.entity;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class DailyHitsTest {

    @Test
    public void now() {
        DailyHits hits = new DailyHits(1);
        String date = hits.getDateAsString();
        assertNotNull(date);
    }

    @Test
    public void dateParsingAndRetrieval() {
        final String expected = "2015-01-07";
        DailyHits hits = new DailyHits(expected, 1);
        String actual = hits.getDateAsString();
        assertThat(actual, is(expected));
    }

}
