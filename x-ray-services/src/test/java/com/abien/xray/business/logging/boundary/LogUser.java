package com.abien.xray.business.logging.boundary;

import javax.inject.Inject;

/**
 *
 * @author blog.adam-bien.com
 */
public class LogUser {
    
    @Inject
    private XRayLogger LOG;
    

    public boolean isLogInjected(){
        return (LOG != null);
    }

    public XRayLogger getLogger() {
        return LOG;
    }
}
