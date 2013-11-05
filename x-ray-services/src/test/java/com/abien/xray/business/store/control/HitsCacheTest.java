package com.abien.xray.business.store.control;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.junit.matchers.JUnitMatchers.hasItem;
import static org.junit.matchers.JUnitMatchers.hasItems;

/**
 *
 * @author adam bien, adam-bien.com
 */
public class HitsCacheTest {

    HitsCache cut;

    @Test
    public void partialStale() {
        ConcurrentMap<String, AtomicLong> initial = new ConcurrentHashMap<String, AtomicLong>();
        initial.put("1", new AtomicLong(1));
        initial.put("2", new AtomicLong(2));
        cut = new HitsCache(initial);
        cut.increase("1");
        Set<String> inactiveEntries = cut.getInactiveEntries();
        assertThat(inactiveEntries, hasItem("2"));
        assertThat(inactiveEntries.size(), is(1));
    }

    @Test
    public void nothingStale() {
        ConcurrentMap<String, AtomicLong> initial = new ConcurrentHashMap<String, AtomicLong>();
        initial.put("1", new AtomicLong(1));
        initial.put("2", new AtomicLong(2));
        cut = new HitsCache(initial);
        Set<String> inactiveEntries = cut.getInactiveEntries();
        assertThat(inactiveEntries, hasItems("1", "2"));
        assertThat(inactiveEntries.size(), is(2));
    }

    @Test
    public void getStaleEntriesAndClear() {
        ConcurrentMap<String, AtomicLong> initial = new ConcurrentHashMap<String, AtomicLong>();
        initial.put("1", new AtomicLong(1));
        initial.put("2", new AtomicLong(2));
        cut = new HitsCache(initial);
        Set<String> inactiveEntries = cut.getInactiveEntries();
        assertThat(inactiveEntries, hasItems("1", "2"));
        assertThat(inactiveEntries.size(), is(2));
        inactiveEntries = cut.getInactiveEntriesAndClear();
        assertThat(inactiveEntries, hasItems("1", "2"));
        assertThat(inactiveEntries.size(), is(2));
        inactiveEntries = cut.getInactiveEntries();
        assertTrue(inactiveEntries.isEmpty());
        assertTrue(cut.getCache().isEmpty());

    }
}
