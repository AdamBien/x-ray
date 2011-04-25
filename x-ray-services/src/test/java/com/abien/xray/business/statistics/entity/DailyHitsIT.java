package com.abien.xray.business.statistics.entity;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * @author Adam Bien <blog.adam-bien.com>
 */
public class DailyHitsIT {

    private static EntityManager em;
    private static EntityTransaction tx;

    @BeforeClass
    public static void initalizeEm() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("integration");
        em = emf.createEntityManager();
        tx = em.getTransaction();
    }

    @Test
    public void persist() {
        DailyHits expected = persist(42);
        long id = expected.getId();
        DailyHits actual = em.find(DailyHits.class, id);
        assertThat(actual, is(expected));
    }

    private DailyHits persist(long hit) {
        DailyHits expected = new DailyHits(new Date(), hit);
        tx.begin();
        this.em.persist(expected);
        tx.commit();
        return expected;
    }

    @Test
    public void findAllDescending() throws InterruptedException {
        persist(1);
        Thread.sleep(1500);
        persist(2);
        Thread.sleep(1500);
        persist(3);
        Thread.sleep(1500);
        List<DailyHits> all = em.createNamedQuery(DailyHits.findAllDescending).getResultList();
        assertFalse(all.isEmpty());
        long hits = 3;
        for (DailyHits hit : all) {
            System.out.println(" " + hit);
            assertThat(hits--, is(hit.getHit()));
        }
    }


    @After
    public void cleanup() {
        em.clear();
        tx.begin();
        em.createQuery("DELETE FROM DailyHits").executeUpdate();
        tx.commit();
    }

}