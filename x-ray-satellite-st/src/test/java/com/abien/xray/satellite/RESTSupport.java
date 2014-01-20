/*
 *//*
 */
package com.abien.xray.satellite;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 *
 * @author adam-bien.com
 */
public class RESTSupport {

    public static JsonObject convertToObjectFrom(String rawValue) {
        JsonReader reader = Json.createReader(new StringReader(rawValue));
        return reader.readObject();
    }

    public static JsonArray convertToArrayFrom(String rawValue) {
        JsonReader reader = Json.createReader(new StringReader(rawValue));
        return reader.readArray();
    }

}
