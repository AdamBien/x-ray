package com.abien.xray.business.monitoring.boundary;

import com.abien.xray.business.monitoring.entity.Diagnostics;
import com.abien.xray.business.monitoring.entity.Invocation;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.*;
import javax.enterprise.event.Observes;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.Resource;

/**
 * @author Adam Bien, blog.adam-bien.com
 */
@Singleton
@Startup
@LocalBean
@Path("monitoring")
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class MonitoringResource implements MonitoringResourceMXBean {


    private MBeanServer platformMBeanServer;
    private ObjectName objectName = null;
    private ConcurrentHashMap<String, Invocation> methods = new ConcurrentHashMap<String, Invocation>();
    private ConcurrentHashMap<String, String> diagnostics = new ConcurrentHashMap<String, String>();
    private AtomicLong exceptionCount;
    
    @Resource
    SessionContext sc;

    @PostConstruct
    public void registerInJMX() {
        this.exceptionCount = new AtomicLong();
        try {
            objectName = new ObjectName("XRayMonitoring:type=" + this.getClass().getName());
            platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
            platformMBeanServer.registerMBean(sc.getBusinessObject(MonitoringResource.class), objectName);
        } catch (Exception e) {
            throw new IllegalStateException("Problem during registration of Monitoring into JMX:" + e);
        }
    }


    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Invocation> getSlowestMethods(@QueryParam("max") @DefaultValue("50") int maxResult) {
        List<Invocation> list = new ArrayList<Invocation>(methods.values());
        Collections.sort(list);
        Collections.reverse(list);
        if (list.size() > maxResult)
            return list.subList(0, maxResult);
        else
            return list;
    }

    @GET
    @Path("exceptionCount")
    @Produces(MediaType.TEXT_PLAIN)
    @Override
    public String getNumberOfExceptions() {
        return String.valueOf(exceptionCount.get());
    }


    public List<Invocation> getSlowestMethods() {
        return getSlowestMethods(50);
    }

    @Override
    public Map<String, String> getDiagnostics() {
        return diagnostics;
    }


    @GET
    @Path("diagnostics")
    @Produces(MediaType.TEXT_PLAIN)
    public String getDiagnosticsAsString() {
        return getDiagnostics().toString();
    }

    @GET
    @Path("diagnostics/{key}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getDiagnosticsForKey(@PathParam("key") String key) {
        return getDiagnostics().get(key);
    }


    public void add(Invocation invocation) {
        String methodName = invocation.getMethodName();
        if (methods.containsKey(methodName)) {
            Invocation existing = methods.get(methodName);
            if (existing.isSlowerThan(invocation)) {
                return;
            }
        }
        methods.put(methodName, invocation);
    }

    public void add(String methodName, long performance) {
        Invocation invocation = new Invocation(methodName, performance);
        this.add(invocation);
    }

    public void exceptionOccurred(String methodName, Exception e) {
        exceptionCount.incrementAndGet();
    }


    public void onNewDiagnostics(@Observes Diagnostics diagnostics) {
        Map<String, String> map = diagnostics.asMap();
        if (map != null) {
            this.diagnostics.putAll(map);
        }
    }

    @Override
    @DELETE
    public void clear() {
        methods.clear();
        exceptionCount.set(0);
        diagnostics.clear();
    }


    @PreDestroy
    public void unregisterFromJMX() {
        try {
            platformMBeanServer.unregisterMBean(this.objectName);
        } catch (Exception e) {
            throw new IllegalStateException("Problem during unregistration of Monitoring into JMX:" + e);
        }
    }
}
