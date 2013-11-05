package com.abien.xray.business.store.control;

import com.abien.xray.business.monitoring.PerformanceAuditor;
import com.abien.xray.business.store.entity.Referer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
@Stateless
@Interceptors(PerformanceAuditor.class)
public class PersistentRefererStore extends PersistentStore {

    @PersistenceContext(unitName = "hitscounter")
    EntityManager em;


    @Override
    void updateStatistics(String id, AtomicLong hitCount) {
        Referer referer = getManaged(id);
        referer.setCount(hitCount.get());

    }

    Referer getManaged(String id) {
        Referer found = em.find(Referer.class, id);
        if (found == null) {
            found = new Referer(id, 0);
            em.merge(found);
        }
        return found;
    }


}
