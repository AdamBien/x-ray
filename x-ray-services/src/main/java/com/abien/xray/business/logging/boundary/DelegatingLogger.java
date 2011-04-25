package com.abien.xray.business.logging.boundary;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.Alternative;

/**
 * @author blog.adam-bien.com
 */
@Alternative
public class DelegatingLogger implements XRayLogger {

    private Logger logger;

    public DelegatingLogger(Logger logger) {
        this.logger = logger;
    }

    public void log(Level level, String message, Object[] params) {
        this.logger.log(level, message, params);
    }

    public Logger getLogger() {
        return logger;
    }

}
