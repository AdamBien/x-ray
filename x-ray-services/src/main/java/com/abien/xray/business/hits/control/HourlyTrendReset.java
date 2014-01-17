/*
 */
package com.abien.xray.business.hits.control;

import com.airhacks.xray.grid.control.Grid;
import com.abien.xray.business.logging.boundary.XRayLogger;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
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
    private ConcurrentMap<String, Long> trending;

    @Schedule(hour = "*/1", persistent = false)
    public void resetTrends() {
        LOG.log(Level.INFO, "Resetting {0} trends", new Object[]{trending.size()});
        trending.clear();
    }

}
