package com.abien.xray.business.logging.boundary;

import com.abien.xray.business.configuration.control.ConfigurationProvider;
import com.abien.xray.business.configuration.boundary.Configuration;

import java.util.logging.Logger;
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
import static org.hamcrest.core.Is.*;

/**
 * @author blog.adam-bien.com
 */
@RunWith(Arquillian.class)
public class LoggerProducerIT {

    @Inject
    LogUser logUser;

    @Inject
    Configuration configuration;

    @Deployment
    public static JavaArchive createTestArchive() {
        return ShrinkWrap.create(JavaArchive.class, "loggerproducer.jar").
                addClasses(LogUser.class, LoggerProducer.class, Configuration.class, ConfigurationProvider.class, DebugConfigurationProvider.class, DevNullLogger.class, DelegatingLogger.class).
                addManifestResource(
                        new ByteArrayAsset("<beans/>".getBytes()),
                        ArchivePaths.create("beans.xml"));
    }

    @Test
    public void logInjected() {
        assertTrue(logUser.isLogInjected());
    }

    @Test
    public void loggerNameCorrespondsToClassNameNoDebug() {
        XRayLogger xRayLogger = logUser.getLogger();
        Logger logger = xRayLogger.getLogger();
        assertTrue(xRayLogger instanceof DelegatingLogger);
        String actual = logger.getName();
        String expected = LogUser.class.getName();
        assertThat(actual, is(expected));

    }
}