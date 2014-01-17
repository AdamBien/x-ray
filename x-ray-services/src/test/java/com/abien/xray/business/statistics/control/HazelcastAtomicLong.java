/*
 */
package com.abien.xray.business.statistics.control;

import com.hazelcast.core.IAtomicLong;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;

/**
 *
 * @author adam-bien.com
 */
public class HazelcastAtomicLong implements IAtomicLong {

    private AtomicLong value;

    public HazelcastAtomicLong() {
        this.value = new AtomicLong(0);
    }

    public final long get() {
        return value.get();
    }

    public final void set(long newValue) {
        value.set(newValue);
    }

    public final void lazySet(long newValue) {
        value.lazySet(newValue);
    }

    public final long getAndSet(long newValue) {
        return value.getAndSet(newValue);
    }

    public final boolean compareAndSet(long expect, long update) {
        return value.compareAndSet(expect, update);
    }

    public final boolean weakCompareAndSet(long expect, long update) {
        return value.weakCompareAndSet(expect, update);
    }

    public final long getAndIncrement() {
        return value.getAndIncrement();
    }

    public final long getAndDecrement() {
        return value.getAndDecrement();
    }

    public final long getAndAdd(long delta) {
        return value.getAndAdd(delta);
    }

    public final long incrementAndGet() {
        return value.incrementAndGet();
    }

    public final long decrementAndGet() {
        return value.decrementAndGet();
    }

    public final long addAndGet(long delta) {
        return value.addAndGet(delta);
    }

    public final long getAndUpdate(LongUnaryOperator updateFunction) {
        return value.getAndUpdate(updateFunction);
    }

    public final long updateAndGet(LongUnaryOperator updateFunction) {
        return value.updateAndGet(updateFunction);
    }

    public final long getAndAccumulate(long x, LongBinaryOperator accumulatorFunction) {
        return value.getAndAccumulate(x, accumulatorFunction);
    }

    public final long accumulateAndGet(long x, LongBinaryOperator accumulatorFunction) {
        return value.accumulateAndGet(x, accumulatorFunction);
    }

    public String toString() {
        return value.toString();
    }

    public int intValue() {
        return value.intValue();
    }

    public long longValue() {
        return value.longValue();
    }

    public float floatValue() {
        return value.floatValue();
    }

    public double doubleValue() {
        return value.doubleValue();
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getPartitionKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getServiceName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
