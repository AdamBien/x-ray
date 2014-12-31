package com.abien.xray.business.logging.boundary;

import java.util.logging.Level;

/**
 *
 * @author blog.adam-bien.com
 */
public interface XRayLogger {

    public void log(Level level, String string, Object[] object);

    public default void log(Level level, String message) {
        this.log(level, message, null);
    }
}
