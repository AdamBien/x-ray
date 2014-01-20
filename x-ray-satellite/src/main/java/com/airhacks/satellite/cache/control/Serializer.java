/*
 */
package com.airhacks.satellite.cache.control;

import java.io.IOException;
import java.io.StringWriter;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;

/**
 *
 * @author adam-bien.com
 */
public class Serializer {

    public static String serialize(JsonObject jsonObject) throws IOException {
        try (final StringWriter stringWriter = new StringWriter()) {
            JsonWriter writer = Json.createWriter(stringWriter);
            writer.writeObject(jsonObject);
            stringWriter.flush();
            return stringWriter.getBuffer().toString();
        }
    }

}
