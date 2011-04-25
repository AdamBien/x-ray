package com.abien.xray.business.logging.boundary;

import java.util.logging.Logger;
import javax.ejb.DependsOn;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author blog.adam-bien.com
 */
public class LoggerProducer {

    @Inject
    private boolean debug;

    @Produces
    public XRayLogger getLogger(InjectionPoint ip) {
        if (debug) {
            Class<?> aClass = ip.getMember().getDeclaringClass();
            Logger logger = Logger.getLogger(aClass.getName());
            return new DelegatingLogger(logger);
        } else {
            return new DevNullLogger();
        }
    }
}
