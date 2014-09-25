/*
 */
package com.abien.xray.business.hits.control;

import javax.json.Json;
import javax.json.JsonObject;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author adam-bien.com
 */
public class JsonSerializerTest {

    @Test
    public void serializeAndDeseserialize() {
        JsonObject expected = Json.createObjectBuilder().add("question", 42).build();
        String serialized = JsonSerializer.serialize(expected);
        assertNotNull(serialized);
        System.out.println("--------------- " + serialized);
        assertTrue(serialized.contains("question"));
        assertTrue(serialized.contains("42"));

        JsonObject actual = JsonSerializer.deserialize(serialized);
        assertThat(actual, is(expected));
    }

}
