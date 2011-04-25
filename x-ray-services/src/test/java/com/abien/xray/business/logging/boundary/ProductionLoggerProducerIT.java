package com.abien.xray.business.logging.boundary;

import com.abien.xray.business.configuration.boundary.Configuration;
import com.abien.xray.business.configuration.control.ConfigurationProvider;

import javax.inject.Inject;

import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * @author blog.adam-bien.com
 */
@RunWith(Arquillian.class)
public class ProductionLoggerProducerIT {

    @Inject
    LogUser logUser;
    @Inject
    Configuration configuration;

    @Deployment
    public static JavaArchive createTestArchive() {
        return ShrinkWrap.create(JavaArchive.class, "loggerproducer.jar").
                addClasses(LogUser.class, LoggerProducer.class, Configuration.class, DevNullLogger.class, DelegatingLogger.class).
                addManifestResource(
                        new ByteArrayAsset("<beans/>".getBytes()),
                        ArchivePaths.create("beans.xml"));
    }

    @Test
    public void productionLoggerInjected() {
        XRayLogger xRayLogger = logUser.getLogger();
        assertTrue(xRayLogger instanceof DevNullLogger);

    }
}