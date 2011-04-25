package com.abien.xray.probe.monitoring;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;


public class JMXRegistry {
  
    private  MBeanServer mbs = null;

    
    public JMXRegistry(){
        this.init();
    }
    
    
    public void init(){
          this.mbs = ManagementFactory.getPlatformMBeanServer();    
    }
   
    /**
     * Registers a JMX bean. The existing MBean with the passed name is going to be
     * unregistered first.
     * @param name - the name in the JMX syntax
     * @param mbean the JMX bean to register
     */
    public void rebind(String name,Object mbean){
        ObjectName mbeanName=null;
        String compositeName=null;
        try {
            compositeName = name + ":type=" + mbean.getClass().getName();
            mbeanName = new ObjectName(compositeName);
        } catch (MalformedObjectNameException ex) {
           throw new IllegalArgumentException("The name:" + compositeName + " is invalid !");
        }
        try {
            if(this.mbs.isRegistered(mbeanName)){
        	this.mbs.unregisterMBean(mbeanName);
            }
            this.mbs.registerMBean(mbean,mbeanName);
        } catch (InstanceAlreadyExistsException ex) {
            throw new IllegalStateException("The mbean: " + mbean.getClass().getName() + " with the name: " + compositeName + " already exists !",ex);
        } catch (NotCompliantMBeanException ex) {
            throw new IllegalStateException("The mbean: " +  mbean.getClass().getName() + " with the name "+ compositeName + " is not compliant JMX bean: " +ex,ex);
        } catch (MBeanRegistrationException ex) {
            throw new RuntimeException("The mbean: " +  mbean.getClass().getName() + " with the name "+ compositeName + " cannot be registered. Reason: " +ex,ex);
        } catch (InstanceNotFoundException ex) {
            throw new RuntimeException("The mbean: " +  mbean.getClass().getName() + " with the name "+ compositeName + " not found - and cannot be deregistered. Reason: " +ex,ex);
        }
    }
     
  }


