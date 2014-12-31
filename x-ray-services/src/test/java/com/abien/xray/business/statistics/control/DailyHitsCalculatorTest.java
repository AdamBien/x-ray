/*
 */
package com.abien.xray.business.statistics.control;

import com.abien.xray.business.hits.control.HitsManagement;
import com.abien.xray.business.logging.boundary.XRayLogger;
import java.util.concurrent.atomic.AtomicLong;
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
        this.cut.LOG = mock(XRayLogger.class);
        this.cut.hits = mock(HitsManagement.class);
        this.cut.hitsAtMidnight = new AtomicLong();
        this.cut.initializeYesterday();

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
    public void dailyComputationWithMidnightSet() {
        final long total = 12;
        when(this.cut.hits.totalHits()).thenReturn(total);
        final int atMidnightTotal = 8;

        this.cut.hitsAtMidnight.set(atMidnightTotal);

        long expected = (total - atMidnightTotal);

        long todayHits = this.cut.getTodayHits();
        assertThat(todayHits, is(expected));

        this.cut.computeDailyHits();

        long afterMidnight = this.cut.getTodayHits();
        assertThat(afterMidnight, is(0l));

        long yesterdayHits = this.cut.hitsAtMidnight.get();
        assertThat(yesterdayHits, is(total));
    }

    @Test
    public void yesterdayComputation() {
        final long total = 12;
        when(this.cut.hits.totalHits()).thenReturn(total);
        final int atMidnightTotal = 8;
        this.cut.hitsAtMidnight.set(atMidnightTotal);

        long todayHits = this.cut.getTodayHits();
        long yesterdayHits = this.cut.getYesterdayHits();
        assertThat(yesterdayHits, is(0l));

        this.cut.computeDailyHits();

        long yesterdayAfterMidnight = this.cut.getYesterdayHits();
        assertThat(todayHits, is(yesterdayAfterMidnight));
    }
}
