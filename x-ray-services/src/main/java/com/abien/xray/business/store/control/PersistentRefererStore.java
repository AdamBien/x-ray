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

    public List<Referer> getMostPopularReferers(int amount) {
        return em.createNamedQuery(Referer.FIND_ALL).setMaxResults(amount).getResultList();
    }

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

    public Map<String, AtomicLong> getReferers() {
        Map<String, AtomicLong> retVal = new HashMap<String, AtomicLong>();
        List<Referer> resultList = this.em.createNamedQuery(Referer.FIND_ALL).getResultList();
        for (Referer referer : resultList) {
            retVal.put(referer.getRefererUri(), new AtomicLong(referer.getCount()));
        }
        return retVal;
    }

    public List<Referer> getMostPopularReferersNotContaining(String uri,int maxNumber){
        return this.em.createNamedQuery(Referer.FIND_ALL_WITHOUT).setParameter("uri", uri).setMaxResults(maxNumber).getResultList();
    }
    
    public void remove(String id){
        Referer referer = this.em.getReference(Referer.class, id);
        this.em.remove(referer);
    }
}
