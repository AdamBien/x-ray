package com.abien.xray.business.logging.boundary;

import com.abien.xray.business.configuration.boundary.Configuration;
import com.abien.xray.business.configuration.control.ConfigurationProvider;
import java.util.logging.Logger;
import javax.inject.Inject;
import static org.hamcrest.core.Is.is;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;

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
        return ShrinkWrap.create(JavaArchive.class).
                addClasses(LogUser.class, LoggerProducer.class, Configuration.class, ConfigurationProvider.class, DebugConfigurationProvider.class, DevNullLogger.class, DelegatingLogger.class).
                addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
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
