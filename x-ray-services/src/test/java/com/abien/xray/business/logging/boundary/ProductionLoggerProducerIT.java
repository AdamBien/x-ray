package com.abien.xray.business.logging.boundary;

import com.abien.xray.business.configuration.boundary.Configuration;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;

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
        return ShrinkWrap.create(JavaArchive.class).
                addClasses(LogUser.class, LoggerProducer.class, Configuration.class, DevNullLogger.class, HazelcastLogger.class).
                addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void productionLoggerInjected() {
        XRayLogger xRayLogger = logUser.getLogger();
        assertTrue(xRayLogger instanceof DevNullLogger);

    }
}
