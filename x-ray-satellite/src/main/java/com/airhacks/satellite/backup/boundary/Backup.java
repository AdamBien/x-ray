package com.airhacks.satellite.backup.boundary;

import com.airhacks.xray.grid.control.GridInstance;
import com.hazelcast.core.IMap;
import java.io.OutputStream;
import java.io.Reader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;

/**
 * @author airhacks.com
 */
@Stateless
public class Backup {

    @Inject
    @Any
    Instance<IMap<String, String>> mapCaches;

    public boolean cacheExists(String name) {
        return (get(name) != null);
    }

    public int size(String name) {
        return get(name).size();
    }

    IMap<String, String> get(String name) {
        GridInstance nameInstance;
        try {
            nameInstance = new GridInstance(name);
        } catch (Exception e) {
            return null;
        }
        final Instance<IMap<String, String>> cacheInstance = mapCaches.select(nameInstance);
        if (cacheInstance.isUnsatisfied() || cacheInstance.isAmbiguous()) {
            return null;
        }
        return cacheInstance.get();

    }

    public void writeMapToStream(String name, OutputStream stream) {

        try (JsonGenerator generator = Json.createGenerator(stream)) {
            IMap<String, String> map = get(name);
            generator.writeStartObject();
            generator.writeStartObject(map.getName());
            map.forEach((key, value) -> {
                generator.write(key, value);
            }
            );
            generator.writeEnd();
            generator.writeEnd();
            generator.flush();
        }
    }

    public Set<String> mapCaches() {
        Set<String> names = new HashSet<>();
        mapCaches.forEach(map -> names.add(map.getName()));
        return names;
    }

    public int store(String cacheName, Reader reader) {
        IMap<String, String> cache = get(cacheName);
        int beforeSize = cache.size();
        parse(reader, cache);
        return cache.size() - beforeSize;
    }

    void parse(Reader reader, Map<String, String> cache) {
        JsonParser parser = Json.createParser(reader);
        String key = null, value = null;
        while (parser.hasNext()) {
            JsonParser.Event event = parser.next();
            switch (event) {
                case START_OBJECT:
                    break;
                case END_OBJECT:
                    cache.put(key, value);
                    break;
                case KEY_NAME:
                    key = parser.getString();
                    break;
                case VALUE_STRING:
                    value = parser.getString();
                    break;
                default:

            }
        }
    }

}
