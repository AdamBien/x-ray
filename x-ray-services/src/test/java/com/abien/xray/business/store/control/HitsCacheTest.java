package com.abien.xray.business.store.control;

import com.abien.xray.business.store.entity.Referer;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author: adam-bien.com
 */
public class HitsCacheTest {

    HitsCache cut;

    @Before
    public void initialize(){
           this.cut = new HitsCache(new ConcurrentHashMap());
    }

    @Test
    public void getMostPopularReferersWithEmptyCache(){
        List<Referer> mostPopularReferers = this.cut.getMostPopularReferers(10);
        assertTrue(mostPopularReferers.isEmpty());
    }

    @Test
    public void getMostPopularReferersWithoutLimit(){
        this.cut.increase("a");
        this.cut.increase("b");
        List<Referer> mostPopularReferers = this.cut.getMostPopularReferers(10);
        assertFalse(mostPopularReferers.isEmpty());
        assertThat(mostPopularReferers.size(),is(2));
        long count = mostPopularReferers.parallelStream().
                filter(f -> "a".equals(f.getRefererUri()) && f.getCount() == 1).
                count();
        assertThat(count,is(1l));

        count = mostPopularReferers.parallelStream().
                filter(f -> "b".equals(f.getRefererUri()) && f.getCount() == 1).
                count();
        assertThat(count,is(1l));
    }

    @Test
    public void getMostPopularReferersWithLimit(){
        this.cut.increase("a");
        this.cut.increase("b");
        this.cut.increase("c");
        List<Referer> mostPopularReferers = this.cut.getMostPopularReferers(2);
        assertThat(mostPopularReferers.size(),is(2));
    }

    @Test
    public void countingOfReferersIsWorking(){
        this.cut.increase("a");
        this.cut.increase("a");
        this.cut.increase("a");
        this.cut.increase("b");
        this.cut.increase("b");
        this.cut.increase("c");
        List<Referer> mostPopularReferers = this.cut.getMostPopularReferers(2);
        System.out.println(mostPopularReferers);
        assertThat(mostPopularReferers.size(),is(2));
        long count = mostPopularReferers.parallelStream().
                filter(f -> "a".equals(f.getRefererUri()) && f.getCount() == 3).
                count();
        assertThat(count,is(1l));
        count = mostPopularReferers.parallelStream().
                filter(f -> "b".equals(f.getRefererUri()) && f.getCount() == 2).
                count();
        assertThat(count,is(1l));

        count = mostPopularReferers.parallelStream().
                filter(f -> "c".equals(f.getRefererUri()) && f.getCount() == 1).
                count();
        assertThat(count,is(0l));
    }

}
