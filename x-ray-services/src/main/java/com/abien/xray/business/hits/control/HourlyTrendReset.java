/*
 */
package com.abien.xray.business.hits.control;

import com.abien.xray.business.grid.control.Grid;
import com.abien.xray.business.logging.boundary.XRayLogger;
import io.quarkus.scheduler.Scheduled;
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
    XRayLogger LOG;

    @Inject
    @Grid(Grid.Name.TRENDING)
    Map<String, String> trending;

    final static String DURATION = "1h";

    @Scheduled(every = HourlyTrendReset.DURATION)
    public void resetTrends() {
        LOG.log(Level.INFO, "Resetting trends");
        trending.clear();
    }

}
