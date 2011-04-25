package com.abien.xray.business.logging.boundary;

import com.abien.xray.business.configuration.control.ConfigurationProvider;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author blog.adam-bien.com
 */
public class DebugConfigurationProvider implements ConfigurationProvider{

    @Override
    public Map<String, String> getConfiguration() {
        return new HashMap<String, String>(){{
            put("debug", "true");
        }};
    }
}
