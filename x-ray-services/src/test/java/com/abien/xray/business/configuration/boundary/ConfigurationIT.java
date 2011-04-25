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
import java.util.Set;
import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;

/**
 * User: blog.adam-bien.com
 * Date: 25.02.11
 * Time: 13:27
 */
@RunWith(Arquillian.class)
public class ConfigurationIT {

    @Inject
    Configurable configurable;

    @Inject
    Configuration configuration;

    @Deployment
    public static JavaArchive createTestArchive() {
        return ShrinkWrap.create(JavaArchive.class, "xray.jar").
                addClasses(Configuration.class).
                addClasses(Configurable.class).
                addClasses(ConfigurationProvider.class).
                addManifestResource(
                        new ByteArrayAsset("<beans/>".getBytes()),
                        ArchivePaths.create("beans.xml"));
    }

    @Test
    public void configurableInjection() {
        assertNotNull(this.configurable);
    }
    
    
    @Test
    public void customConfigurationNotExist(){
        assertFalse(this.configuration.doesCustomConfigurationExist());
    }


    @Test
    public void versionInjection() {
        assertNotNull(this.configurable.getVersion());
    }

    @Test
    public void notExistingParameter() {
        assertNull(this.configurable.getNotExistingAndEmpty());
        Set<String> unconfiguredFields = this.configuration.getUnconfiguredFields();
        assertNotNull(unconfiguredFields);
        assertThat(unconfiguredFields.size(), is(2));
    }
    
    @Test
    public void booleanInjection(){
        assertFalse(this.configurable.isDebug());
    }
}
