/*
 */
package com.airhacks.satellite.cache.control;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;

/**
 *
 * @author adam-bien.com
 */
public class EntryListenerAdapter implements EntryListener<String, String> {

    @Override
    public void entryAdded(EntryEvent<String, String> ee) {
        System.out.println("entryAdded: " + ee);
    }

    @Override
    public void entryRemoved(EntryEvent<String, String> ee) {
        System.out.println("entryRemoved: " + ee);
    }

    @Override
    public void entryUpdated(EntryEvent<String, String> ee) {
        System.out.println("entryUpdated: " + ee);

    }

    @Override
    public void entryEvicted(EntryEvent<String, String> ee) {
        System.out.println("entryEvicted: " + ee);
    }

}
