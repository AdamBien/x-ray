/*
 */
package com.abien.xray.business.hits.control;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class HitsManagementTest {

    HitsManagement cut;

    @Before
    public void init() {
        this.cut = new HitsManagement();
        this.cut.initCache();
    }


    @Test
    public void mergeCounter() {
        long value = this.cut.updateHitsForURI("id", 1);
        assertThat(value, is(1l));

        value = this.cut.updateHitsForURI("id", 2);
        assertThat(value, is(3l));

    }

}
