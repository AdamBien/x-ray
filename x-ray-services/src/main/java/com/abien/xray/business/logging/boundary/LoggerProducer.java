package com.abien.xray.business.logging.boundary;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * @author blog.adam-bien.com
 */
@Singleton
public class LoggerProducer {

    @Inject
    @ConfigProperty(name="debug",defaultValue="false")
    boolean debug;

    @Produces
    public XRayLogger getLogger(InjectionPoint ip) {
        if (debug) {
            final String name = ip.getMember().getDeclaringClass().getName();
            return new SystemOutLogger(name);
        } else {
            return new DevNullLogger();
        }
    }
}
