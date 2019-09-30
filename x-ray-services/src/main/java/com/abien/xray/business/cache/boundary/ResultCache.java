package com.abien.xray.business.cache.boundary;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
@Dependent
public class ResultCache<T> {

    private T recentResult;

    @Inject
    ExecutorService resultCachePool;

    @PostConstruct
    public void init() {
        this.resultCachePool = Executors.newFixedThreadPool(10);
    }


    public T getCachedValueOr(Supplier<T> slowCalculation, T defaultValue) {
        CompletableFuture.supplyAsync(slowCalculation, resultCachePool).
                thenAccept(this::storeComputation);
        if (this.recentResult == null) {
            return defaultValue;
        }
        return this.recentResult;
    }

    void storeComputation(T currentResult) {
        this.recentResult = currentResult;
    }

}
