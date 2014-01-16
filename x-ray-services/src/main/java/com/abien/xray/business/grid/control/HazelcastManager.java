package com.abien.xray.business.grid.control;

import com.hazelcast.core.Cluster;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import java.util.Date;
import java.util.Map;
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
    private IMap<String, JsonObject> firehose;
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
        this.firehose = this.hazelcast.getMap("firehose");
        this.titles = this.hazelcast.getMap("titles");
    }

    @Produces
    public HazelcastInstance create() {
        return this.hazelcast;
    }

    @Produces
    @Grid(Grid.Name.HITS)
    public Map<String, Long> exposeHits() {
        return this.hits;
    }

    @Produces
    @Grid(Grid.Name.TRENDING)
    public Map<String, Long> exposeTrending() {
        return this.trending;
    }

    @Produces
    @Grid(Grid.Name.DAILY)
    public Map<Date, Long> exposeDaily() {
        return this.daily;
    }

    @Produces
    @Grid(Grid.Name.REFERERS)
    public Map<Date, Long> exposeReferers() {
        return this.daily;
    }

    @Produces
    @Grid(Grid.Name.TITLES)
    public Map<String, String> titles() {
        return this.titles;
    }

    @Produces
    @Grid(Grid.Name.FIREHOSE)
    public Map<String, JsonObject> exposeFirehose() {
        return this.firehose;
    }

    @PreDestroy
    public void shutdown() {
        Hazelcast.shutdownAll();
    }

}
