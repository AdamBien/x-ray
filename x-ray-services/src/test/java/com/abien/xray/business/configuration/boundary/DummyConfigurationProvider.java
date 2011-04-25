package com.abien.xray.business.configuration.boundary;

import com.abien.xray.business.configuration.control.ConfigurationProvider;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author blog.adam-bien.com
 */
public class DummyConfigurationProvider implements ConfigurationProvider{

    @Override
    public Map<String, String> getConfiguration() {
        Map<String,String> configuration = new HashMap<String, String>();
        configuration.put("version", "testversion");
        return configuration;
    }

}
