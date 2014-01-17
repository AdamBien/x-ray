/*
 */
package com.abien.xray.business.hits.control;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author adam-bien.com
 */
public class HitsCacheTest {

    HitsCache cut;
    ConcurrentMap<String, String> map;

    @Before
    public void init() {
        this.map = new ConcurrentHashMap<>();
        this.cut = new HitsCache(this.map);
    }

    @Test
    public void cacheSize() {
        this.map.put("1", "1");
        this.map.put("2", "2");
        int cacheSize = this.cut.getCacheSize();
        assertThat(cacheSize, is(2));
    }

    @Test
    public void totalCalculation() {
        this.map.put("1", "1");
        this.map.put("2", "2");
        long totalHits = this.cut.getTotalHits();
        assertThat(totalHits, is(3l));
    }

}
