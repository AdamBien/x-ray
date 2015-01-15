package com.abien.xray.business.logging.boundary;

import java.text.MessageFormat;
import java.util.logging.Level;
import javax.enterprise.inject.Alternative;

/**
 * @author blog.adam-bien.com
 */
@Alternative
public class SystemOutLogger implements XRayLogger {

    private final String origin;

    public SystemOutLogger(String name) {
        this.origin = name;
    }

    public void log(Level level, String message, Object[] params) {
        System.out.println(this.origin + ":" + getLevelAsString(level) + serialize(message, params));
    }

    String serialize(String message, Object[] params) {
        return MessageFormat.format(message, params);
    }

    @Override
    public void log(Level level, String message) {
        System.out.println(this.origin + ":" + getLevelAsString(level) + message);
    }

    String getLevelAsString(Level level) {
        return level.getName() + ":";
    }

}
