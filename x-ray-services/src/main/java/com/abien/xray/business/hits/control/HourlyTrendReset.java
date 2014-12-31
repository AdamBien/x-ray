/*
 */
package com.abien.xray.business.hits.control;

import com.abien.xray.business.logging.boundary.XRayLogger;
import com.airhacks.xray.grid.control.Grid;
import java.util.logging.Level;
import javax.cache.Cache;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

/**
 *
 * @author adam-bien.com
 */
@Singleton
public class HourlyTrendReset {

    @Inject
    private XRayLogger LOG;

    @Inject
    @Grid(Grid.Name.TRENDING)
    private Cache<String, String> trending;

    @Schedule(hour = "*/1", persistent = false)
    public void resetTrends() {
        LOG.log(Level.INFO, "Resetting trends");
        trending.clear();
    }

}
