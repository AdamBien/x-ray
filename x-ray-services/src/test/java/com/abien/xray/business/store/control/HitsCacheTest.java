package com.abien.xray.business.store.control;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.matchers.JUnitMatchers.*;
import static org.hamcrest.CoreMatchers.*;


/**
 *
 * @author adam bien, adam-bien.com
 */
public class HitsCacheTest {

    HitsCache cut;
    
    
    @Test
    public void partialStale() {
        Map<String,AtomicLong> initial = new HashMap<String, AtomicLong>();
        initial.put("1", new AtomicLong(1));
        initial.put("2", new AtomicLong(2));
        cut = new HitsCache(initial);
        cut.increase("1");
        Set<String> inactiveEntries = cut.getInactiveEntries();
        assertThat(inactiveEntries,hasItem("2"));
        assertThat(inactiveEntries.size(),is(1));
    }

    @Test
    public void nothingStale() {
        Map<String,AtomicLong> initial = new HashMap<String, AtomicLong>();
        initial.put("1", new AtomicLong(1));
        initial.put("2", new AtomicLong(2));
        cut = new HitsCache(initial);
        Set<String> inactiveEntries = cut.getInactiveEntries();
        assertThat(inactiveEntries,hasItems("1","2"));
        assertThat(inactiveEntries.size(),is(2));
    }

    @Test
    public void getStaleEntriesAndClear() {
        Map<String,AtomicLong> initial = new HashMap<String, AtomicLong>();
        initial.put("1", new AtomicLong(1));
        initial.put("2", new AtomicLong(2));
        cut = new HitsCache(initial);
        Set<String> inactiveEntries = cut.getInactiveEntries();
        assertThat(inactiveEntries,hasItems("1","2"));
        assertThat(inactiveEntries.size(),is(2));
        inactiveEntries = cut.getInactiveEntriesAndClear();
        assertThat(inactiveEntries,hasItems("1","2"));
        assertThat(inactiveEntries.size(),is(2));
        inactiveEntries = cut.getInactiveEntries();
        assertTrue(inactiveEntries.isEmpty());
        assertTrue(cut.getCache().isEmpty());

    }
}
