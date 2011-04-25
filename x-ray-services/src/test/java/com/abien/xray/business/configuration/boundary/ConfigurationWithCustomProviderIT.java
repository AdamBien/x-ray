package com.abien.xray.business.configuration.boundary;

import com.abien.xray.business.configuration.control.ConfigurationProvider;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;


import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;

/**
 * User: blog.adam-bien.com
 * Date: 25.02.11
 * Time: 13:27
 */
@RunWith(Arquillian.class)
public class ConfigurationWithCustomProviderIT {

    @Inject
    Configurable configurable;

    @Inject
    Configuration configuration;

    @Deployment
    public static JavaArchive createTestArchive() {
        return ShrinkWrap.create(JavaArchive.class, "xray.jar").
                addClasses(Configuration.class,Configurable.class,ConfigurationProvider.class,DummyConfigurationProvider.class,AnotherConfigurationProvider.class).
                addManifestResource(
                        new ByteArrayAsset("<beans/>".getBytes()),
                        ArchivePaths.create("beans.xml"));
    }

    
    
    @Test
    public void customConfigurationExist(){
        assertTrue(this.configuration.doesCustomConfigurationExist());
    }


    @Test
    public void versionInjection() {
        String expected = new DummyConfigurationProvider().getConfiguration().get("version");
        assertThat(this.configurable.getVersion(),is(expected));
    }

    @Test
    public void answerKeyInjection() {
        String expectedString = new AnotherConfigurationProvider().getConfiguration().get("answer");
        int expected = Integer.parseInt(expectedString);
        assertThat(this.configurable.getAnswer(),is(expected));
    }

}
