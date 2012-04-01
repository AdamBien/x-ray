package com.abien.xray.business.scavenger.control;

import com.abien.xray.business.monitoring.PerformanceAuditor;
import com.abien.xray.business.store.boundary.Cache;
import com.abien.xray.business.store.control.HitsCache;
import com.abien.xray.business.store.control.PersistentRefererStore;
import java.util.Set;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

/**
 *
 * @author adam bien, adam-bien.com
 */
@Interceptors(PerformanceAuditor.class)
public class RefererScavenger {

    @Inject @Cache
    HitsCache hitsCache;
    
    @Inject
    PersistentRefererStore store;
    
    public void removeInactiveReferers() {
        Set<String> inactiveEntriesAndRemove = hitsCache.getInactiveEntriesAndClear();
        for (String string : inactiveEntriesAndRemove) {
            store.remove(string);
        }
    }
    
}
