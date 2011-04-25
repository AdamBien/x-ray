package com.abien.xray.business.configuration.control;

import java.util.Map;

/**
 *
 * @author blog.adam-bien.com
 */
public interface ConfigurationProvider {
    
    public Map<String,String> getConfiguration();

}
