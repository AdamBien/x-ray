package com.abien.xray.probe.monitoring;

import com.abien.xray.probe.http.HTTPRequestRESTInterceptor;

/**
 *
 * @author blog.adam-bien.com
 */
public class XRayMonitoring implements XRayMonitoringMBean{
    
    public final static String JMX_NAME = XRayMonitoring.class.getName();

    @Override
    public int getNrOfRejectedJobs() {
         return HTTPRequestRESTInterceptor.getNrOfRejectedJobs();
    }

    @Override
    public long getXRayPerformance() {
        return HTTPRequestRESTInterceptor.getXRayPerformance();
    }

    @Override
    public long getApplicationPerformance() {
        return HTTPRequestRESTInterceptor.getApplicationPerformance();
    }

    @Override
    public void reset() {
        HTTPRequestRESTInterceptor.resetStatistics();
    }

    @Override
    public long getWorstXRayPerformance() {
        return HTTPRequestRESTInterceptor.getWorstXRayPerformance();
    }

    @Override
    public long getWorstApplicationPerformance() {
        return HTTPRequestRESTInterceptor.getWorstApplicationPerformance();
    }
}
