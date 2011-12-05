package com.abien.xray.business.monitoring.boundary;

import com.abien.xray.business.monitoring.entity.Invocation;
import java.util.List;
import java.util.Map;

/**
 * User: blog.adam-bien.com
 * Date: 14.02.11
 * Time: 20:56
 */
public interface MonitoringResourceMXBean {
    List<Invocation> getSlowestMethods();
    Map<String,String> getDiagnostics();
    String getNumberOfExceptions();
    void clear();
}
