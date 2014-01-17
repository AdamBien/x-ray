/*
 */
package com.airhacks.satellite.cache.boundary;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.monitor.LocalMapStats;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author adam-bien.com
 */
@Startup
@Singleton
public class CacheExposer {

    private HazelcastInstance hazelcast;

    private IMap<String, String> hits;
    private IMap<String, String> referers;
    private IMap<String, String> trending;

    @PostConstruct
    public void initialize() {
        this.hazelcast = Hazelcast.newHazelcastInstance();
        this.hits = this.hazelcast.getMap("hits");
        this.hits.addEntryListener(new EntryListenerAdapter(), true);
        this.referers = this.hazelcast.getMap("referers");
        this.referers.addEntryListener(new EntryListenerAdapter(), true);
        this.trending = this.hazelcast.getMap("trending");
        this.trending.addEntryListener(new EntryListenerAdapter(), true);
    }

    public LocalMapStats getStatistics(String name) {
        return this.hazelcast.getMap(name).getLocalMapStats();
    }

    @PreDestroy
    public void shutdown() {
        Hazelcast.shutdownAll();
    }
}
