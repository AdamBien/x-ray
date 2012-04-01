package com.abien.xray.business.scavenger.control;

import com.abien.xray.business.monitoring.PerformanceAuditor;
import com.abien.xray.business.monitoring.entity.Diagnostics;
import com.abien.xray.business.store.boundary.Cache;
import com.abien.xray.business.store.control.HitsCache;
import com.abien.xray.business.store.control.PersistentRefererStore;
import java.util.Date;
import java.util.Set;
import javax.enterprise.event.Event;
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
    
    @Inject
    Event<Diagnostics> monitoring;
    
    public void removeInactiveReferers() {
        Set<String> inactiveEntriesAndRemove = hitsCache.getInactiveEntriesAndClear();
        int nbrOfInactiveReferes = inactiveEntriesAndRemove.size();
        for (String string : inactiveEntriesAndRemove) {
            store.remove(string);
        }
        this.sendDiagnostics(nbrOfInactiveReferes);
    }

    void sendDiagnostics(long nbrOfInactiveReferers){
        Diagnostics diagnostics = Diagnostics.with("scavengingStart", new Date()).
        and("inactiveReferersToScavenge", nbrOfInactiveReferers);
        monitoring.fire(diagnostics);
    }
}
