/*
 */
package com.airhacks.xray.persistence.control;

import com.hazelcast.core.MapStore;
import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;

/**
 *
 * @author adam-bien.com
 */
public class MapDBStore implements MapStore<String, String> {

    private String storeName;
    private DB storeDB;
    private final static String STORE_FOLDER = "/store";
    private final static String STORE_FILE = "/hazelcast.db";
    private final BTreeMap<String, String> store;

    public MapDBStore(String storeName) {
        this(STORE_FOLDER, STORE_FILE, storeName);
    }

    public MapDBStore(String folder, String file, String storeName) {
        this.storeName = file;
        this.storeDB = DBMaker.newFileDB(new File(folder, file))
                .closeOnJvmShutdown()
                .make();
        this.store = this.storeDB.getTreeMap(storeName);
    }

    @Override
    public void store(String k, String v) {
        this.store.put(k, v);
    }

    @Override
    public void storeAll(Map<String, String> map) {
        this.store.putAll(map);
    }

    @Override
    public void delete(String k) {
        this.store.remove(k);
    }

    @Override
    public void deleteAll(Collection<String> keys) {
        keys.stream().forEach(key -> this.store.remove(key));
    }

    @Override
    public String load(String k) {
        return this.store.get(k);
    }

    @Override
    public Map<String, String> loadAll(Collection<String> clctn) {
        return this.store;
    }

    @Override
    public Set<String> loadAllKeys() {
        return this.store.keySet();
    }

}
