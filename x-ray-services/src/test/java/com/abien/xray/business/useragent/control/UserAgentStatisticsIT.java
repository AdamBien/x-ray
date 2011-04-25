package com.abien.xray.business.useragent.control;

import com.abien.xray.business.useragent.entity.UserAgent;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * User: blog.adam-bien.com
 * Date: 07.01.11
 * Time: 13:59
 */

public class UserAgentStatisticsIT {
    private EntityManager em;
    private EntityTransaction tx;

    UserAgentStatistics cut;


    @Before
    public void initializeCUT(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("integration");
        em = emf.createEntityManager();
        tx = em.getTransaction();

       this.cut = new UserAgentStatistics();
       this.cut.em = em;
    }

    @Test
    public void persist() {
        String agent = "agent";
        UserAgent expected = persist(agent);
        UserAgent actual = em.find(UserAgent.class, agent);
        assertThat(actual, is(expected));
    }

    @Test
    public void getMostPopularAgentsWithEmtpyDB(){
        List<UserAgent> mostPopularAgents = this.cut.getMostPopularAgents(2);
        assertTrue(mostPopularAgents.isEmpty());
    }

    @Test
    public void getMostPopularAgentsSorting(){
        UserAgent first = persist("first");
        UserAgent second = persist("second");
        first.increaseCounter();
        tx.begin();
        //sync with DB
        tx.commit();
        List<UserAgent> mostPopularAgents = this.cut.getMostPopularAgents(2);
        assertFalse(mostPopularAgents.isEmpty());
        Iterator<UserAgent> userAgentIterator = mostPopularAgents.iterator();
        UserAgent actual = userAgentIterator.next();
        assertThat(actual,is(first));
        actual = userAgentIterator.next();
        assertThat(actual,is(second));

    }

    private UserAgent persist(String agent) {
        UserAgent expected = new UserAgent(agent);
        tx.begin();
        this.em.persist(expected);
        tx.commit();
        return expected;
    }

    @After
    public void cleanup(){
        tx.begin();
        em.createQuery("delete from UserAgent").executeUpdate();
        tx.commit();
        em.clear();
    }
}
