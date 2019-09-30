package com.abien.xray.business.grid.control;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 *
 * @author adam-bien.com
 */
@ApplicationScoped
public class GridManager {

    private Queue<String> rejected;
    private Queue<String> firehose;
    private Map<String, String> exceptions;
    private Map<String, String> hits;
    private Map<String, String> trending;
    private Map<String, String> daily;
    private Map<String, String> referers;
    private Map<String, String> titles;
    private Map<String, String> diagnostics;
    private Map<String, String> methods;
    private Map<String, String> filters;
    static final int MAX_QUEUE_CAPACITY = 1000;

    @PostConstruct
    public void init() {

        this.firehose = new LinkedBlockingDeque<>(MAX_QUEUE_CAPACITY);
        this.rejected = new LinkedBlockingDeque<>(MAX_QUEUE_CAPACITY);
        this.hits = new ConcurrentHashMap<>();
        this.trending = new ConcurrentHashMap<>();
        this.daily = new ConcurrentHashMap<>();
        this.referers = new ConcurrentHashMap<>();
        this.titles = new ConcurrentHashMap<>();
        this.exceptions = new ConcurrentHashMap<>();
        this.diagnostics = new ConcurrentHashMap<>();
        this.methods = new ConcurrentHashMap<>();
        this.filters = new ConcurrentHashMap<>();
    }

    @Produces
    @Grid(Grid.Name.FILTERS)
    public Map<String, String> exposeFilters() {
        return this.filters;
    }

    @Produces
    @Grid(Grid.Name.HITS)
    public Map<String, String> exposeHits() {
        return this.hits;
    }

    @Produces
    @Grid(Grid.Name.TRENDING)
    public Map<String, String> exposeTrending() {
        return this.trending;
    }

    @Produces
    @Grid(Grid.Name.DAILY)
    public Map<String, String> exposeDaily() {
        return this.daily;
    }

    @Produces
    @Grid(Grid.Name.REFERERS)
    public Map<String, String> exposeReferers() {
        return this.referers;
    }

    @Produces
    @Grid(Grid.Name.TITLES)
    public Map<String, String> titles() {
        return this.titles;
    }

    @Produces
    @Grid(Grid.Name.EXCEPTIONS)
    public Map<String, String> exposeExceptions() {
        return this.exceptions;
    }

    @Produces
    @Grid(Grid.Name.DIAGNOSTICS)
    public Map<String, String> exposeDiagnostics() {
        return this.diagnostics;
    }

    @Produces
    @Grid(Grid.Name.METHODS)
    public Map<String, String> exposeMethods() {
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

}
