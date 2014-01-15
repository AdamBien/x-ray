package com.abien.xray.business.logging.boundary;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.LoggingService;
import javax.annotation.PostConstruct;
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

    @Inject
    HazelcastInstance instance;
    private String LOGGING = "logging";

    private LoggingService logging;

    @PostConstruct
    public void init() {
        this.logging = this.instance.getLoggingService();
    }

    @Produces
    public XRayLogger getLogger(InjectionPoint ip) {
        if (debug) {
            Class<?> aClass = ip.getMember().getDeclaringClass();
            ILogger logger = this.logging.getLogger(aClass);
            return new HazelcastLogger(logger);
        } else {
            return new DevNullLogger();
        }
    }
}
