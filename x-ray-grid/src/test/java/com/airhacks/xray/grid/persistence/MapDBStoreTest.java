/*
 */
package com.airhacks.xray.grid.persistence;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author adam-bien.com
 */
public class MapDBStoreTest {

    private MapDBStore cut;

    private String tempFileName;
    static final String STORE_FOLDER = "./target";

    @Before
    public void computeFileName() {
        this.tempFileName = "file-" + System.nanoTime() + ".hazel";
    }

    @Test
    public void crud() {
        final String KEY = "duke";
        final String VALUE = "java";
        this.cut = new MapDBStore(STORE_FOLDER, this.tempFileName, "TEST_MAP");
        assertTrue(this.cut.loadAllKeys().isEmpty());
        this.cut.store(KEY, VALUE);
        int size = this.cut.loadAllKeys().size();
        assertThat(size, is(1));
        String value = this.cut.load(KEY);
        assertThat(value, is(VALUE));
        this.cut.delete(KEY);
        assertTrue(this.cut.loadAllKeys().isEmpty());
    }

}
