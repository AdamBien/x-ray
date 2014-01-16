/*
 */
package com.abien.xray.business.hits.control;

import com.abien.xray.business.grid.control.Grid;
import com.abien.xray.business.logging.boundary.XRayLogger;
import com.abien.xray.business.monitoring.PerformanceAuditor;
import java.io.StringReader;
import java.util.Queue;
import java.util.logging.Level;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author adam-bien.com
 */
@Singleton
@Interceptors(PerformanceAuditor.class)
public class InboundQueueProcessor {

    @Inject
    @Grid(Grid.Name.FIREHOSE)
    private Queue<String> firehose;

    @Inject
    URLPathExtractor extractor;

    @Inject
    private XRayLogger LOG;

    @Inject
    HitsManagement management;

    @Schedule(second = "*/5", persistent = false)
    public void processRequests() {
        LOG.log(Level.INFO, "Processing queue with depth {0}", new Object[]{firehose.size()});
        firehose.stream().forEach(r -> processURL(r));
    }

    void processURL(String payload) {
        JsonObject object = Json.createReader(new StringReader(payload)).readObject();
        this.processURL(object.getString("url"), object);
    }

    void processURL(String url, JsonObject headerMap) {
        String uniqueAction = extractor.extractPathSegmentFromURL(url);
        LOG.log(Level.INFO, "updateStatistics({0}) - extracted uniqueAction: {1}", new Object[]{url, uniqueAction});
        String referer = extractor.extractReferer(url);
        LOG.log(Level.INFO, "updateStatistics({0}) - extracted referer: {1}", new Object[]{url, referer});
        management.updateStatistics(uniqueAction, referer, headerMap);
    }

}
