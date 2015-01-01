package com.abien.xray.business.monitoring.boundary;

import com.abien.xray.business.grid.control.Grid;
import com.abien.xray.business.monitoring.entity.Diagnostics;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.cache.Cache;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

/**
 * @author Adam Bien, blog.adam-bien.com
 */
@Singleton
@Startup
@LocalBean
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class HealthMonitor implements HealthMonitorMXBean {

    private MBeanServer platformMBeanServer;
    private ObjectName objectName = null;

    @Inject
    @Grid(Grid.Name.METHODS)
    Cache<String, String> methods;

    @Inject
    @Grid(Grid.Name.DIAGNOSTICS)
    Cache<String, String> diagnostics;

    @Inject
    @Grid(Grid.Name.EXCEPTIONS)
    Cache<String, String> exceptions;

    AtomicLong totalExceptionCount;

    @Resource
    SessionContext sc;

    @PostConstruct
    public void registerInJMX() {
        this.totalExceptionCount = new AtomicLong();
        try {
            objectName = new ObjectName("XRayMonitoring:type=" + this.getClass().getName());
            platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
            platformMBeanServer.registerMBean(sc.getBusinessObject(HealthMonitor.class), objectName);
        } catch (IllegalStateException | InstanceAlreadyExistsException | MBeanRegistrationException | MalformedObjectNameException | NotCompliantMBeanException e) {
            throw new IllegalStateException("Problem during registration of Monitoring into JMX:" + e);
        }
    }

    @Override
    public Map<String, String> getDiagnostics() {
        return StreamSupport.stream(diagnostics.spliterator(), false).collect(Collectors.toMap(f -> f.getKey(), f -> f.getValue()));
    }

    public void add(String methodName, long performance) {
        this.methods.put(methodName, String.valueOf(performance));
    }

    public void exceptionOccurred(String methodName, Exception e) {
        totalExceptionCount.incrementAndGet();
        this.exceptions.put(methodName, e.toString());
    }

    public void onNewDiagnostics(@Observes Diagnostics diagnostics) {
        Map<String, String> map = diagnostics.asMap();
        if (map != null) {
            this.diagnostics.putAll(map);
        }
    }

    @Override
    public void clear() {
        methods.clear();
        totalExceptionCount.set(0);
        diagnostics.clear();
    }

    @PreDestroy
    public void unregisterFromJMX() {
        try {
            platformMBeanServer.unregisterMBean(this.objectName);
        } catch (InstanceNotFoundException | MBeanRegistrationException e) {
            throw new IllegalStateException("Problem during unregistration of Monitoring into JMX:" + e);
        }
    }

    @Override
    public String getNumberOfExceptions() {
        return String.valueOf(this.totalExceptionCount.get());
    }

    @Override
    public List<String> getSlowestMethods() {
        return StreamSupport.stream(this.methods.spliterator(), false).sorted((left, right) -> {
            return new Long(Long.parseLong(right.getValue())).compareTo(Long.parseLong(left.getValue()));
        }).map(e -> e.getKey() + " :" + e.getValue() + " ms").
                collect(Collectors.toList());
    }

}
