package com.abien.xray.business.store.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Referer {

    private String refererUri;
    private long count;
    

    public Referer() { /*...JPA*/}

    public Referer(String referer, long count) {
        this.refererUri = referer;
        this.count = count;
    }

    public String getRefererUri() {
        return refererUri;
    }

    public void setRefererUri(String refererUri) {
        this.refererUri = refererUri;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    
}
