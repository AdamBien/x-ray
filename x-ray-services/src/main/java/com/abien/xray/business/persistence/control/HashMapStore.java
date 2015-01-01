/*
 */
package com.airhacks.xray.persistence.control;

import com.hazelcast.core.MapStore;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.Vetoed;

/**
 *
 * @author adam-bien.com
 */
@Vetoed
public class HashMapStore implements MapStore<String, String> {

    private String storeName;
    private final static String STORE_FOLDER = "/store/";
    private final static String STORE_FILE = "-serialized.db";
    private HashMap<String, String> store;
    static final String DBFOLDER_KEY = "db.folder";
    static final String DBFILE_KEY = "db.file";

    public final static boolean DEBUG = false;
    private final String folder;
    private final String file;

    public HashMapStore(String storeName, Properties properties) {
        this(properties.getProperty(DBFOLDER_KEY, STORE_FOLDER),
                properties.getProperty(DBFILE_KEY, STORE_FILE),
                storeName);
    }

    public HashMapStore(String folder, String file, String storeName) {
        System.out.printf("################ store created: folder: %s, file: %s, storeName: %s", folder, file, storeName);
        this.storeName = storeName;
        this.folder = folder;
        this.file = file;
        try {
            this.store = (HashMap<String, String>) FilePersistence.deserialize(getFile());
        } catch (IOException | ClassNotFoundException ex) {
            this.store = new HashMap<>();
        }
        System.out.printf("%d entries loaded", this.store.size());
    }

    @Override
    public void store(String k, String v) {
        if (DEBUG) {
            System.out.printf("Storing %s -> %s", k, v);
        }
        this.store.put(k, v);
        this.save();
    }

    @Override
    public void storeAll(Map<String, String> map) {
        this.store.putAll(map);
        this.save();
    }

    @Override
    public void delete(String k) {
        this.store.remove(k);
        this.save();
    }

    @Override
    public void deleteAll(Collection<String> keys) {
        keys.stream().forEach(key -> this.store.remove(key));
        this.save();
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

    void save() {
        try {
            FilePersistence.serialize(getFile(), this.store);
        } catch (IOException ex) {
            Logger.getLogger(HashMapStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    String getFile() {
        return this.folder + this.storeName + this.file;
    }

}
