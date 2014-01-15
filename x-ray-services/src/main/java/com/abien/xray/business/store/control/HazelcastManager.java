package com.abien.xray.business.store.control;

import com.hazelcast.core.Cluster;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;

/**
 *
 * @author adam-bien.com
 */
@Startup
@Singleton
public class HazelcastManager {

    private HazelcastInstance hazelcast;
    private Cluster cluster;

    @PostConstruct
    public void init() {
        this.hazelcast = Hazelcast.newHazelcastInstance();
        this.cluster = this.hazelcast.getCluster();
    }

    @Produces
    public HazelcastInstance create() {
        return this.hazelcast;
    }

    @PreDestroy
    public void shutdown() {
        Hazelcast.shutdownAll();
    }

}
