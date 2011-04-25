package com.abien.xray.business.monitoring;

import com.abien.xray.business.logging.boundary.XRayLogger;
import com.abien.xray.business.monitoring.boundary.MonitoringResource;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.AroundTimeout;
import javax.interceptor.InvocationContext;
import java.util.logging.Level;

/**
 * @author Adam Bien, blog.adam-bien.com
 */
public class PerformanceAuditor {

    @Inject
    private XRayLogger LOG;

    @EJB
    MonitoringResource monitoring;

    @AroundTimeout
    @AroundInvoke
    public Object measurePerformance(InvocationContext context) throws Exception {
        String methodName = context.getMethod().toString();
        long start = System.currentTimeMillis();
        try {
            return context.proceed();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "!!!During invocation of: {0} exception occured: {1}", new Object[]{methodName, e});
            monitoring.exceptionOccurred(methodName, e);
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - start;
            monitoring.add(methodName, duration);
        }
    }
}
