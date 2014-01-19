/*
 */
package com.abien.xray.business.hits.control;

import javax.json.Json;
import javax.json.JsonObject;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author adam-bien.com
 */
public class JsonSerializerTest {

    @Test
    public void serialize() {
        JsonObject object = Json.createObjectBuilder().add("question", 42).build();
        String serialized = JsonSerializer.serialize(object);
        assertNotNull(serialized);
        System.out.println("--------------- " + serialized);
        assertTrue(serialized.contains("question"));
        assertTrue(serialized.contains("42"));
    }

}
