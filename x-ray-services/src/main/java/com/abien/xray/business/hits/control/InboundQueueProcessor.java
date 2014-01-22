/*
 */
package com.abien.xray.business.hits.control;

import com.abien.xray.business.logging.boundary.XRayLogger;
import com.abien.xray.business.monitoring.PerformanceAuditor;
import com.airhacks.xray.grid.control.Grid;
import com.hazelcast.core.IQueue;
import java.io.StringReader;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author adam-bien.com
 */
@Startup
@Singleton
@Interceptors(PerformanceAuditor.class)
public class InboundQueueProcessor {

    @Inject
    @Grid(Grid.Name.FIREHOSE)
    private IQueue<String> firehose;

    @Inject
    URLPathExtractor extractor;

    @Inject
    private XRayLogger LOG;

    @Inject
    HitsManagement management;

    @Schedule(hour = "*", minute = "*", second = "*/5", persistent = false)
    public void processRequests() {
        try {
            LOG.log(Level.INFO, "Processing queue with depth {0}", new Object[]{firehose.size()});
            String content = null;
            while ((content = firehose.poll(1, TimeUnit.SECONDS)) != null) {
                processURL(content);
            }
        } catch (InterruptedException ex) {
            LOG.log(Level.WARNING, "Waited 1 second and gave up!");
        }
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
