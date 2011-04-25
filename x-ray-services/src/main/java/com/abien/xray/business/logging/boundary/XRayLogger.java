package com.abien.xray.business.logging.boundary;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author blog.adam-bien.com
 */
public interface XRayLogger {

    public void log(Level INFO, String string, Object[] object);

    public Logger getLogger();

}
