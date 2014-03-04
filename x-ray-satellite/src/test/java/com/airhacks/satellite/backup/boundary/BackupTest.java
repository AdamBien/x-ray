/*
 */
package com.airhacks.satellite.backup.boundary;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
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
        String backupData = "{\"hits\":{\"/entry/how_to_self_invoke_ejb\":\"12\",\"/entry/real_world_java_ee_patterns3\":\"3\"}}";
        StringReader reader = new StringReader(backupData);
        Map<String, String> map = new HashMap<>();
        this.cut.parse(reader, map);
        assertThat(map.size(), is(2));
        System.out.println("Map: " + map);
        String result = map.get("/entry/how_to_self_invoke_ejb");
        assertNotNull(result);
        assertThat(result, is(String.valueOf(12)));

        result = map.get("/entry/real_world_java_ee_patterns3");
        assertNotNull(result);
        assertThat(result, is(String.valueOf(3)));

    }
}
