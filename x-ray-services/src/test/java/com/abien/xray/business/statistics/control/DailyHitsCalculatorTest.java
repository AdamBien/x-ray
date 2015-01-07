/*
 */
package com.abien.xray.business.statistics.control;

import com.abien.xray.business.CacheMock;
import com.abien.xray.business.hits.control.HitsManagement;
import com.abien.xray.business.logging.boundary.XRayLogger;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
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

    Map<String, String> data;
    static final long YESTERDAY_HIT = 2;

    @Before
    public void initAndMock() {
        this.data = new HashMap<>();
        this.cut = new DailyHitsCalculator();
        this.cut.LOG = mock(XRayLogger.class);
        this.cut.hits = mock(HitsManagement.class);
        this.cut.totalHitsAtMidnight = new AtomicLong();
        this.cut.dailyHistory = new CacheMock(this.data);
    }

    public void populate() {
        LocalDate today = LocalDate.of(2014, Month.JANUARY, 3);
        LocalDate yesterday = LocalDate.of(2014, Month.JANUARY, 2);
        this.data.put(yesterday.format(DateTimeFormatter.ISO_DATE), String.valueOf(YESTERDAY_HIT));
        this.data.put(today.format(DateTimeFormatter.ISO_DATE), "3");

    }

    @Test
    public void dailyComputationWithYesterdayNull() {
        this.cut.initializeYesterday();

        final long total = 5l;
        when(this.cut.hits.totalHits()).thenReturn(total);

        long todayHits = this.cut.getTodayHits();
        assertThat(todayHits, is(total));

        this.cut.computeDailyHits();

        long afterMidnight = this.cut.getTodayHits();
        assertThat(afterMidnight, is(0l));

        long yesterdayHits = this.cut.totalHitsAtMidnight.get();
        assertThat(yesterdayHits, is(todayHits));
    }

    @Test
    public void dailyComputationWithMidnightSet() {
        this.populate();
        this.cut.initializeYesterday();
        final long total = 12;
        when(this.cut.hits.totalHits()).thenReturn(total);
        final int atMidnightTotal = 8;

        this.cut.totalHitsAtMidnight.set(atMidnightTotal);

        long expected = (total - atMidnightTotal);

        long todayHits = this.cut.getTodayHits();
        assertThat(todayHits, is(expected));

        this.cut.computeDailyHits();

        long afterMidnight = this.cut.getTodayHits();
        assertThat(afterMidnight, is(0l));

        long yesterdayHits = this.cut.totalHitsAtMidnight.get();
        assertThat(yesterdayHits, is(total));
    }

    @Test
    public void getYesterdayHitsFromHistory() {
        this.populate();
        this.cut.initializeYesterday();

        long actual = this.cut.getYesterdayHitsFromHistory();
        assertThat(actual, is(YESTERDAY_HIT));
    }

}
