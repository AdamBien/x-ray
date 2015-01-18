package com.abien.xray.business.hits.control;

import com.airhacks.porcupine.execution.control.ThreadFactoryExposer;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
import javax.enterprise.inject.Specializes;
import javax.ws.rs.Produces;

/**
 *
 * @author airhacks.com
 */
public class SpecializedThreadFactoryExposer extends ThreadFactoryExposer {

    public final static AtomicLong counter = new AtomicLong(0);

    @Override
    @Specializes
    @Produces
    public ThreadFactory expose() {
        return (r) -> new Thread(r, "-SpecializedThreadFactoryExposer-" + counter.incrementAndGet());
    }

}
