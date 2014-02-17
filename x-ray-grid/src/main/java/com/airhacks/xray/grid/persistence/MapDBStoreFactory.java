/*
 */
package com.airhacks.xray.grid.persistence;

import com.hazelcast.core.MapLoader;
import com.hazelcast.core.MapStoreFactory;
import java.util.Properties;

/**
 *
 * @author adam-bien.com
 */
public class MapDBStoreFactory implements MapStoreFactory<String, String> {

    @Override
    public MapLoader<String, String> newMapStore(String storeName, Properties prprts) {
        return new MapDBStore(storeName);
    }

}
