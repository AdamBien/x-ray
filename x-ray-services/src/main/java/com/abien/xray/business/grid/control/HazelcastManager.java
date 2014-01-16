package com.abien.xray.business.grid.control;

import com.hazelcast.core.Cluster;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.IQueue;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.json.JsonObject;

/**
 *
 * @author adam-bien.com
 */
@Startup
@Singleton
public class HazelcastManager {

    private HazelcastInstance hazelcast;
    private Cluster cluster;

    private IMap<String, Long> hits;
    private IMap<String, Long> trending;
    private IMap<Date, Long> daily;
    private IQueue<JsonObject> firehose;
    private IMap<String, Long> referers;
    private IMap<String, String> titles;

    @PostConstruct
    public void init() {
        this.hazelcast = Hazelcast.newHazelcastInstance();
        this.cluster = this.hazelcast.getCluster();
        this.hits = this.hazelcast.getMap("hits");
        this.trending = this.hazelcast.getMap("trending");
        this.daily = this.hazelcast.getMap("daily");
        this.referers = this.hazelcast.getMap("referers");
        this.firehose = this.hazelcast.getQueue("firehose");
        this.titles = this.hazelcast.getMap("titles");
    }

    @Produces
    public HazelcastInstance create() {
        return this.hazelcast;
    }

    @Produces
    @Grid(Grid.Name.HITS)
    public ConcurrentMap<String, Long> exposeHits() {
        return this.hits;
    }

    @Produces
    @Grid(Grid.Name.TRENDING)
    public ConcurrentMap<String, Long> exposeTrending() {
        return this.trending;
    }

    @Produces
    @Grid(Grid.Name.DAILY)
    public ConcurrentMap<Date, Long> exposeDaily() {
        return this.daily;
    }

    @Produces
    @Grid(Grid.Name.REFERERS)
    public ConcurrentMap<String, Long> exposeReferers() {
        return this.referers;
    }

    @Produces
    @Grid(Grid.Name.TITLES)
    public ConcurrentMap<String, String> titles() {
        return this.titles;
    }

    @Produces
    @Grid(Grid.Name.FIREHOSE)
    public Queue<JsonObject> exposeFirehose() {
        return this.firehose;
    }

    @PreDestroy
    public void shutdown() {
        Hazelcast.shutdownAll();
    }

}
