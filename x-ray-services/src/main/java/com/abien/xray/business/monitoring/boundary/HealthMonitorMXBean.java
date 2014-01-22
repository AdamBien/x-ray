package com.abien.xray.business.monitoring.boundary;

import java.util.List;
import java.util.Map;

/**
 * User: blog.adam-bien.com Date: 14.02.11 Time: 20:56
 */
public interface HealthMonitorMXBean {

    Map<String, String> getDiagnostics();

    List<String> getSlowestMethods();

    String getNumberOfExceptions();

    void clear();
}
