package com.abien.xray.business.configuration.boundary;

import com.abien.xray.business.configuration.control.ConfigurationProvider;
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
 * User: blog.adam-bien.com Date: 25.02.11 Time: 13:27
 */
@RunWith(Arquillian.class)
public class ConfigurationWithCustomProviderIT {

    @Inject
    Configurable configurable;

    @Inject
    Configuration configuration;

    @Deployment
    public static JavaArchive createTestArchive() {
        return ShrinkWrap.create(JavaArchive.class).
                addClasses(Configuration.class, Configurable.class, ConfigurationProvider.class, DummyConfigurationProvider.class, AnotherConfigurationProvider.class).
                addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void customConfigurationExist() {
        assertTrue(this.configuration.doesCustomConfigurationExist());
    }

    @Test
    public void versionInjection() {
        String expected = new DummyConfigurationProvider().getConfiguration().get("version");
        assertThat(this.configurable.getVersion(), is(expected));
    }

    @Test
    public void answerKeyInjection() {
        String expectedString = new AnotherConfigurationProvider().getConfiguration().get("answer");
        int expected = Integer.parseInt(expectedString);
        assertThat(this.configurable.getAnswer(), is(expected));
    }

}
