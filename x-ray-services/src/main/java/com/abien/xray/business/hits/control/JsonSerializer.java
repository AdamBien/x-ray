/*
 */
package com.abien.xray.business.hits.control;

import java.io.IOException;
import java.io.StringWriter;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;

/**
 *
 * @author adam-bien.com
 */
public class JsonSerializer {

    public static String serialize(JsonObject object) {
        String representation = null;
        try (StringWriter stringWriter = new StringWriter()) {
            JsonWriter jsonWriter = Json.createWriter(stringWriter);
            jsonWriter.writeObject(object);
            stringWriter.flush();
            representation = stringWriter.getBuffer().toString();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot close writer");
        }
        return representation;
    }

}
