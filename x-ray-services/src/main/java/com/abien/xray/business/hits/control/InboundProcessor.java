/*
 */
package com.abien.xray.business.hits.control;

import com.abien.xray.business.logging.boundary.XRayLogger;
import com.abien.xray.business.monitoring.PerformanceAuditor;
import com.airhacks.porcupine.execution.control.Managed;
import java.io.StringReader;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author adam-bien.com
 */
@Interceptors(PerformanceAuditor.class)
public class InboundProcessor {

    @Inject
    URLPathExtractor extractor;

    @Inject
    private XRayLogger LOG;

    @Inject
    HitsManagement management;

    @Inject
    @Managed(maxPoolSize = 10, pipelineName = "inbound-processor", queueCapacity = 100)
    ExecutorService inbound;

    public void processURL(String payload) {
        inbound.execute(() -> {
            JsonObject object = Json.createReader(new StringReader(payload)).readObject();
            this.processURL(object.getString("url"), object);
        });

    }

    void processURL(String url, JsonObject headerMap) {
        String uniqueAction = extractor.extractPathSegmentFromURL(url);
        LOG.log(Level.INFO, "updateStatistics({0}) - extracted uniqueAction: {1}", new Object[]{url, uniqueAction});
        String referer = extractor.extractReferer(url);
        LOG.log(Level.INFO, "updateStatistics({0}) - extracted referer: {1}", new Object[]{url, referer});
        management.updateStatistics(uniqueAction, referer, headerMap);
    }

}
