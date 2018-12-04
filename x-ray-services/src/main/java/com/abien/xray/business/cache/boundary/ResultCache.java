package com.abien.xray.business.cache.boundary;

import com.airhacks.porcupine.execution.boundary.Dedicated;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
public class ResultCache<T> {

    private T recentResult;

    @Inject
    @Dedicated
    ExecutorService resultCachePool;

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
