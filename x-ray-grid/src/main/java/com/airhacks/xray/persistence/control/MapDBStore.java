/*
 */
package com.airhacks.xray.persistence.control;

import com.hazelcast.core.MapStore;
import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.enterprise.inject.Vetoed;
import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;

/**
 *
 * @author adam-bien.com
 */
@Vetoed
public class MapDBStore implements MapStore<String, String> {

    private String storeName;
    private DB storeDB;
    private final static String STORE_FOLDER = "/store";
    private final static String STORE_FILE = "/hazelcast.db";
    private final BTreeMap<String, String> store;
    static final String DBFOLDER_KEY = "db.folder";
    static final String DBFILE_KEY = "db.file";

    public final static boolean DEBUG = false;

    public MapDBStore(String storeName, Properties properties) {
        this(properties.getProperty(DBFOLDER_KEY, STORE_FOLDER),
                properties.getProperty(DBFILE_KEY, STORE_FILE),
                storeName);
    }

    public MapDBStore(String folder, String file, String storeName) {
        System.out.printf("################ store created: folder: %s, file: %s, storeName: %s", folder, file, storeName);
        this.storeName = file;
        this.storeDB = DBMaker.newFileDB(new File(folder, file))
                .closeOnJvmShutdown()
                .make();
        this.store = this.storeDB.getTreeMap(storeName);
        System.out.printf("%d entries loaded", this.store.size());
    }

    @Override
    public void store(String k, String v) {
        if (DEBUG) {
            System.out.printf("Storing %s -> %s", k, v);
        }
        this.store.put(k, v);
        this.storeDB.commit();
    }

    @Override
    public void storeAll(Map<String, String> map) {
        this.store.putAll(map);
        this.storeDB.commit();
    }

    @Override
    public void delete(String k) {
        this.store.remove(k);
        this.storeDB.commit();
    }

    @Override
    public void deleteAll(Collection<String> keys) {
        keys.stream().forEach(key -> this.store.remove(key));
        this.storeDB.commit();
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
