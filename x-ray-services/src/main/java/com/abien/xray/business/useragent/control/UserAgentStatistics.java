package com.abien.xray.business.useragent.control;

import com.abien.xray.business.useragent.entity.UserAgent;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.HttpHeaders;
import java.util.List;
import java.util.Map;

/**
 * User: blog.adam-bien.com
 * Date: 07.01.11
 * Time: 13:21
 */
@Stateless
public class UserAgentStatistics {

    @PersistenceContext
    EntityManager em;

    public void updateStatistics(String userAgent){
        UserAgent agent = em.find(UserAgent.class, userAgent);
        if(agent == null){
            agent = new UserAgent(userAgent);
            em.persist(agent);
        }
        agent.increaseCounter();
    }

    public List<UserAgent> getMostPopularAgents(int maxResult){
        return em.createNamedQuery(UserAgent.FIND_ALL).setMaxResults(maxResult).getResultList();
    }

    public void extractAndStoreReferer(Map<String, String> headerMap) {
        String userAgentHeaderName = "xray_" + HttpHeaders.USER_AGENT.toLowerCase();
        String userAgent = headerMap.get(userAgentHeaderName);
        updateStatistics(userAgent);
    }
}
