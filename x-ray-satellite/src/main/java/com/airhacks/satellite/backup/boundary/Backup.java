package com.airhacks.satellite.backup.boundary;

import com.airhacks.xray.grid.control.GridInstance;
import com.hazelcast.core.IMap;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.stream.JsonGenerator;

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

    public int writeMapToStream(String name, OutputStream stream) {

        JsonGenerator generator = Json.createGenerator(stream);
        GridInstance selector = new GridInstance(name);
        Instance<IMap<String, String>> mapInstance = mapCaches.select(selector);
        IMap<String, String> map = mapInstance.get();
        generator.writeStartObject();
        map.forEach((key, value) -> {
            generator.write(key, value);
        }
        );
        generator.writeEnd();
        try {
            stream.flush();
            return map.size();
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot flush", ex);
        }
    }

    public Set<String> mapCaches() {
        Set<String> names = new HashSet<>();
        mapCaches.forEach(map -> names.add(map.getName()));
        return names;
    }

}
