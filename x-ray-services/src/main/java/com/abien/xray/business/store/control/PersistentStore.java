package com.abien.xray.business.store.control;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
public abstract class PersistentStore {

    /**
     *  It is not asynchronous on purpose. This method is invoked by a timer. It
     *  is better to delay a timer, than cause multiple, concurrent timer invocations.
     */
    public void store(Map<String,AtomicLong> cache){
        Set<Entry<String, AtomicLong>> entrySet = cache.entrySet();
        for (Entry<String, AtomicLong> entry : entrySet) {
            String id = entry.getKey();
            AtomicLong hitCount = entry.getValue();
            updateStatistics(id,hitCount);
        }
    }

    abstract void updateStatistics(String id, AtomicLong hitCount);
}
