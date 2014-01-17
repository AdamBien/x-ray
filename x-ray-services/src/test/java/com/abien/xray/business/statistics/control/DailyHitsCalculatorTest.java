/*
 */
package com.abien.xray.business.statistics.control;

import com.abien.xray.business.hits.control.HitsManagement;
import com.abien.xray.business.logging.boundary.XRayLogger;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author adam-bien.com
 */
public class DailyHitsCalculatorTest {

    DailyHitsCalculator cut;

    @Before
    public void initAndMock() {
        this.cut = new DailyHitsCalculator();
        this.cut.hitsAtMidnight = new HazelcastAtomicLong();
        this.cut.hits = mock(HitsManagement.class);
        this.cut.LOG = mock(XRayLogger.class);
    }

    @Test
    public void dailyComputationWithYesterdayNull() {
        final long total = 1l;
        when(this.cut.hits.totalHits()).thenReturn(total);

        long todayHits = this.cut.getTodayHits();
        assertThat(todayHits, is(total));

        this.cut.computeDailyHits();

        long afterMidnight = this.cut.getTodayHits();
        assertThat(afterMidnight, is(0l));

        long yesterdayHits = this.cut.hitsAtMidnight.get();
        assertThat(yesterdayHits, is(todayHits));
    }

    @Test
    public void dailyComputationWithYesterdaySet() {
        final long total = 12;
        when(this.cut.hits.totalHits()).thenReturn(total);
        final int yesterday = 8;

        this.cut.hitsAtMidnight.set(yesterday);

        long expected = (total - yesterday);

        long todayHits = this.cut.getTodayHits();
        assertThat(todayHits, is(expected));

        this.cut.computeDailyHits();

        long afterMidnight = this.cut.getTodayHits();
        assertThat(afterMidnight, is(0l));

        long yesterdayHits = this.cut.hitsAtMidnight.get();
        assertThat(yesterdayHits, is(total));
    }

}
