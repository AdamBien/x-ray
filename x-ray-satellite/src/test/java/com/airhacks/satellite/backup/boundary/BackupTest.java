/*
 */
package com.airhacks.satellite.backup.boundary;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class BackupTest {

    Backup cut;

    @Before
    public void init() {
        this.cut = new Backup();
    }

    @Test
    public void parse() {
        String backupData = "{\"hits\":{\"42\":\"{\\\"content\\\":\\\"the answer\\\"}\"}}";
        StringReader reader = new StringReader(backupData);
        Map<String, String> map = new HashMap<>();
        this.cut.parse(reader, map);
        assertThat(map.size(), is(1));
        String result = map.get("42");
        System.out.println("Result: " + result);
        Assert.assertNotNull(result);
    }
}
