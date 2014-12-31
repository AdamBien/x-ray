package com.abien.xray.business.logging.boundary;

import java.util.logging.Logger;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author blog.adam-bien.com
 */
@Singleton
public class LoggerProducer {

    @Inject
    private boolean debug;

    @Produces
    public XRayLogger getLogger(InjectionPoint ip) {
        if (debug) {
            final String name = ip.getMember().getDeclaringClass().getName();
            return Logger.getLogger(name)::log;
        } else {
            return new DevNullLogger();
        }
    }
}
