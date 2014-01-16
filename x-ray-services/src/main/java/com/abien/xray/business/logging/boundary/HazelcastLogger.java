package com.abien.xray.business.logging.boundary;

import com.hazelcast.logging.ILogger;
import java.text.MessageFormat;
import java.util.logging.Level;
import javax.enterprise.inject.Alternative;

/**
 * @author blog.adam-bien.com
 */
@Alternative
public class HazelcastLogger implements XRayLogger {

    private final ILogger logger;

    HazelcastLogger(ILogger logger) {
        this.logger = logger;
    }

    public void log(Level level, String message, Object[] params) {
        logger.log(level, serialize(message, params));
    }

    String serialize(String message, Object[] params) {
        return MessageFormat.format(message, params);
    }

    @Override
    public void log(Level level, String message) {
        logger.log(level, message);
    }

}
