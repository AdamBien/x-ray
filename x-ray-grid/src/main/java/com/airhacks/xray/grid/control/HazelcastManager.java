package com.airhacks.xray.grid.control;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.Configuration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
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

    private Queue<String> rejected;
    private Queue<String> firehose;
    private Cache<String, String> exceptions;
    private Cache<String, String> hits;
    private Cache<String, String> trending;
    private Cache<String, String> daily;
    private Cache<String, String> referers;
    private Cache<String, String> titles;
    private Cache<String, String> diagnostics;
    private Cache<String, String> methods;
    private Cache<String, String> filters;
    private CachingProvider cachingProvider;
    private CacheManager cacheManager;
    static final int MAX_QUEUE_CAPACITY = 1000;

    @PostConstruct
    public void init() {
        this.cachingProvider = Caching.getCachingProvider();
        this.cacheManager = cachingProvider.getCacheManager();
        Configuration configuration = getConfiguration();

        this.firehose = new LinkedBlockingDeque<>(MAX_QUEUE_CAPACITY);
        this.rejected = new LinkedBlockingDeque<>(MAX_QUEUE_CAPACITY);
        this.hits = this.cacheManager.createCache("hits", configuration);
        this.trending = this.cacheManager.createCache("trending", configuration);
        this.daily = this.cacheManager.createCache("daily", configuration);
        this.referers = this.cacheManager.createCache("referers", configuration);
        this.titles = this.cacheManager.createCache("titles", configuration);
        this.exceptions = this.cacheManager.createCache("exceptions", configuration);
        this.diagnostics = this.cacheManager.createCache("diagnostics", configuration);
        this.methods = this.cacheManager.createCache("methods", configuration);
        this.filters = this.cacheManager.createCache("filters", configuration);
    }

    public Configuration<String, String> getConfiguration() {
        MutableConfiguration<String, String> configuration = new MutableConfiguration<>();
        configuration.setStoreByValue(false).
                setTypes(String.class, String.class).
                setManagementEnabled(true).
                setStatisticsEnabled(true);
        return configuration;
    }

    @Produces
    @Grid(Grid.Name.FILTERS)
    public Cache<String, String> exposeFilters() {
        return this.filters;
    }

    @Produces
    @Grid(Grid.Name.HITS)
    public Cache<String, String> exposeHits() {
        return this.hits;
    }

    @Produces
    @Grid(Grid.Name.TRENDING)
    public Cache<String, String> exposeTrending() {
        return this.trending;
    }

    @Produces
    @Grid(Grid.Name.DAILY)
    public Cache<String, String> exposeDaily() {
        return this.daily;
    }

    @Produces
    @Grid(Grid.Name.REFERERS)
    public Cache<String, String> exposeReferers() {
        return this.referers;
    }

    @Produces
    @Grid(Grid.Name.TITLES)
    public Cache<String, String> titles() {
        return this.titles;
    }

    @Produces
    @Grid(Grid.Name.EXCEPTIONS)
    public Cache<String, String> exposeExceptions() {
        return this.exceptions;
    }

    @Produces
    @Grid(Grid.Name.DIAGNOSTICS)
    public Cache<String, String> exposeDiagnostics() {
        return this.diagnostics;
    }

    @Produces
    @Grid(Grid.Name.METHODS)
    public Cache<String, String> exposeMethods() {
        return this.methods;
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

    @PreDestroy
    public void shutdown() {
        this.cacheManager.close();
        this.cachingProvider.close();
    }

}
