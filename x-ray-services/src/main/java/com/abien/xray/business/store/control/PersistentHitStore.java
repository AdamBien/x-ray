package com.abien.xray.business.store.control;

import com.abien.xray.business.logging.boundary.XRayLogger;
import com.abien.xray.business.monitoring.PerformanceAuditor;
import com.abien.xray.business.store.entity.Hit;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import javax.inject.Inject;

/**
 * @author Adam Bien, blog.adam-bien.com
 */
@Stateless
@Interceptors(PerformanceAuditor.class)
public class PersistentHitStore extends PersistentStore {

    @Inject
    private XRayLogger LOG;

    @PersistenceContext(unitName = "hitscounter")
    EntityManager em;

    public List<Hit> getMostPopularURLs(int max) {
        return em.createNamedQuery(Hit.FIND_ALL).setMaxResults(max).getResultList();
    }

    public List<Hit> getMostPopularURLs(String excludeContaining, int max) {
        return em.createNamedQuery(Hit.FIND_ALL_WITHOUT).setParameter("uri", excludeContaining).setMaxResults(max).getResultList();
    }

    public List<Hit> getMostPopularPosts(int max) {
        return em.createNamedQuery(Hit.FIND_ALL_WITH).setParameter("uri", "/entry/%").setMaxResults(max).getResultList();
    }


    @Override
    public void updateStatistics(String id, AtomicLong hitCount) {
        Hit hit = getManaged(id);
        hit.setCount(hitCount.get());
    }

    public void invalidate(String uri) {
        getManaged(uri).invalidate();

    }

    public Hit getManaged(String id) {
        Hit found = em.find(Hit.class, id);
        if (found == null) {
            found = new Hit(id, 0);
            found = em.merge(found);
        }
        return found;
    }

    public Hit find(String id) {
        return em.find(Hit.class, id);
    }

    public Map<String, AtomicLong> getHits() {
        Map<String, AtomicLong> retVal = new HashMap<String, AtomicLong>();
        List<Hit> resultList = this.em.createNamedQuery(Hit.FIND_ALL).getResultList();
        for (Hit hit : resultList) {
            retVal.put(hit.getActionId(), new AtomicLong(hit.getCount()));
        }
        return retVal;
    }

}
