package com.airhacks.satellite.backup.boundary;

import com.airhacks.satellite.backup.UnknownCacheException;
import com.airhacks.xray.grid.control.GridInstance;
import com.hazelcast.core.IMap;
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

    public void writeMapToStream(String name, OutputStream stream) throws UnknownCacheException {
        JsonGenerator generator = Json.createGenerator(stream);
        GridInstance selector = null;
        try {
            selector = new GridInstance(name);
        } catch (Exception e) {
            throw new UnknownCacheException("Cache: " + name + " does not exist");
        }
        Instance<IMap<String, String>> mapInstance = mapCaches.select(selector);
        if (mapInstance.isUnsatisfied()) {
            throw new UnknownCacheException("Cache: " + name + " does not exist");
        }
        IMap<String, String> map = mapInstance.get();
        map.forEach((key, value) -> {
            generator.write(key, value);
        }
        );
    }

    public Set<String> mapCaches() {
        Set<String> names = new HashSet<>();
        mapCaches.forEach(map -> names.add(map.getName()));
        return names;
    }

}
