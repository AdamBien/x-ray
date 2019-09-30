/*
 */
package com.abien.xray.business.hits.control;

import com.abien.xray.business.grid.control.Grid;
import com.abien.xray.business.logging.boundary.XRayLogger;
import java.util.Map;
import java.util.logging.Level;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author adam-bien.com
 */
@ApplicationScoped
public class HourlyTrendReset {

    @Inject
    private XRayLogger LOG;

    @Inject
    @Grid(Grid.Name.TRENDING)
    private Map<String, String> trending;

    //@Schedule(hour = "*/1", persistent = false)
    public void resetTrends() {
        LOG.log(Level.INFO, "Resetting trends");
        trending.clear();
    }

}
