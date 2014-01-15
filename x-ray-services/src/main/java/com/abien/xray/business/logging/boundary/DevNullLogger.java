package com.abien.xray.business.logging.boundary;

import java.util.logging.Level;
import javax.enterprise.inject.Alternative;

/**
 * @author blog.adam-bien.com
 */
@Alternative
public class DevNullLogger implements XRayLogger {

    @Override
    public void log(Level INFO, String string, Object[] object) {
        //ignore everything
    }
}
