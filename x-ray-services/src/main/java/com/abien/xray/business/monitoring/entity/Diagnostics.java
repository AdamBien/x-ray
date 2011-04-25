package com.abien.xray.business.monitoring.entity;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author adam-bien.com
 */
public class Diagnostics {
    
    private Map<String,String> parameters = null;
    
    private Diagnostics(String name,Object value){
        this.parameters = new HashMap<String,String>();
        this.parameters.put(name, String.valueOf(value));
    }
    public static Diagnostics with(String name,Object value){
        return new Diagnostics(name, value);
    }
    public Diagnostics and(String name,Object value){
        this.parameters.put(name, String.valueOf(value));
        return this;
    }
    public Map<String,String> asMap(){
        return this.parameters;
    }
}
