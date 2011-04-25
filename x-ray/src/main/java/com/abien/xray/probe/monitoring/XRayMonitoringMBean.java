package com.abien.xray.probe.monitoring;

/**
 *
 * @author blog.adam-bien.com
 */
public interface XRayMonitoringMBean {

    public int getNrOfRejectedJobs();
    public long getXRayPerformance();
    public long getWorstXRayPerformance();
    public long getWorstApplicationPerformance();
    public long getApplicationPerformance();
    public void reset();
}
