package com.abien.xray.business.cache.boundary;

import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.LongStream;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class ResultCacheTest {

    ResultCache<Long> cut;
    private Iterator<Long> longStream;

    @Before
    public void init() {
        this.cut = new ResultCache<>();
        this.longStream = LongStream.range(1, 10).iterator();
        this.cut.resultCachePool = Executors.newCachedThreadPool();
    }

    @Test
    public void getCachedValue() {
        final long defaultValue = 42l;
        Long uninitialized = this.cut.getCachedValueOr(this::nextValue, defaultValue);
        assertThat(uninitialized, is(defaultValue));
        slowDown(20);
        Long cached = this.cut.getCachedValueOr(this::nextValue, defaultValue);
        assertThat(cached, is(1l));
        slowDown(20);
        cached = this.cut.getCachedValueOr(this::nextValue, defaultValue);
        assertThat(cached, is(2l));

    }

    public long nextValue() {
        slowDown(10);
        return longStream.next();
    }

    void slowDown(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            Logger.getLogger(ResultCacheTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
