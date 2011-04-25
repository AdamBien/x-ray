package com.abien.xray.business.configuration.boundary;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * User: blog.adam-bien.com
 * Date: 25.02.11
 * Time: 13:32
 */
@Singleton
public class Configurable {
    @Inject
    private String version;

    @Inject
    private String notExistingAndEmpty;
    
    @Inject
    private boolean debug;
    
    @Inject
    private int answer;

    public String getVersion() {
        return version;
    }

    public String getNotExistingAndEmpty() {
        return notExistingAndEmpty;
    }

    public int getAnswer() {
        return answer;
    }

    public boolean isDebug() {
        return debug;
    }
}
