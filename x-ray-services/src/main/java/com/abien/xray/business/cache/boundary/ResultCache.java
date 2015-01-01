package com.abien.xray.business.cache.boundary;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;

/**
 *
 * @author airhacks.com
 */
public class ResultCache<T> {

    private T recentResult;

    @Resource
    ManagedExecutorService mes;

    public T getCachedValueOr(Supplier<T> slowCalculation, T defaultValue) {
        CompletableFuture.supplyAsync(slowCalculation, mes).thenAccept(this::storeComputation);
        if (this.recentResult == null) {
            this.recentResult = defaultValue;
        }
        return this.recentResult;
    }

    void storeComputation(T currentResult) {
        this.recentResult = currentResult;
    }

}
