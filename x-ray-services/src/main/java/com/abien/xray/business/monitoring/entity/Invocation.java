package com.abien.xray.business.monitoring.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: blog.adam-bien.com
 * Date: 14.02.11
 * Time: 19:28
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Invocation implements Comparable<Invocation>{

    @XmlAttribute
    private String methodName;
    @XmlAttribute
    private Long invocationPerformance;

    public Invocation(String methodName, long invocationPerformance) {
        this.methodName = methodName;
        this.invocationPerformance = invocationPerformance;
    }


    public Invocation() { /* JAXB...*/}

    public String getMethodName() {
        return methodName;
    }

    public long getInvocationPerformance() {
        return invocationPerformance;
    }

    public boolean isSlowerThan(Invocation invocation){
        return this.compareTo(invocation) > 0;
    }

    @Override
    public int compareTo(Invocation anotherInvocation){
        return this.invocationPerformance.compareTo(anotherInvocation.invocationPerformance);
    }

    @Override
    public int hashCode() {
        return methodName != null ? methodName.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Invocation that = (Invocation) o;

        if (methodName != null ? !methodName.equals(that.methodName) : that.methodName != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Invocation{" +
                "methodName='" + methodName + '\'' +
                ", invocationPerformance=" + invocationPerformance +
                '}';
    }
}
