package com.airhacks.xray.grid.control;

import com.hazelcast.core.Cluster;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;
import com.hazelcast.core.IMap;
import com.hazelcast.core.IQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 *
 * @author adam-bien.com
 */
@Startup
@Singleton
public class HazelcastManager {

    private HazelcastInstance hazelcast;
    private Cluster cluster;

    private IQueue<String> rejected;
    private IQueue<String> firehose;
    private IMap<String, String> hits;
    private IMap<String, String> trending;
    private IMap<String, String> daily;
    private IMap<String, String> referers;
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
        this.rejected = this.hazelcast.getQueue("rejected");
    }

    @Produces
    public HazelcastInstance create() {
        return this.hazelcast;
    }

    @Produces
    @Grid(Grid.Name.HITS)
    public ConcurrentMap<String, String> exposeHits() {
        return this.hits;
    }

    @Produces
    @Grid(Grid.Name.TRENDING)
    public ConcurrentMap<String, String> exposeTrending() {
        return this.trending;
    }

    @Produces
    @Grid(Grid.Name.DAILY)
    public ConcurrentMap<String, String> exposeDaily() {
        return this.daily;
    }

    @Produces
    @Grid(Grid.Name.REFERERS)
    public ConcurrentMap<String, String> exposeReferers() {
        return this.referers;
    }

    @Produces
    @Grid(Grid.Name.TITLES)
    public ConcurrentMap<String, String> titles() {
        return this.titles;
    }

    @Produces
    @Grid(Grid.Name.FIREHOSE)
    public Queue<String> exposeFirehose() {
        return this.firehose;
    }

    @Produces
    @Grid(Grid.Name.REJECTED)
    public Queue<String> exposeRejected() {
        return this.rejected;
    }

    @Produces
    public IAtomicLong exposeLong(InjectionPoint ip) {
        String name = ip.getMember().getName();
        return hazelcast.getAtomicLong(name);
    }

    @PreDestroy
    public void shutdown() {
        Hazelcast.shutdownAll();
    }

}
