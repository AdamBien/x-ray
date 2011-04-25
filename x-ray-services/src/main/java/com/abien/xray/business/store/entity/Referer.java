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
@Entity
@Table(name="X_REFERER")
@NamedQueries({
@NamedQuery(name=Referer.FIND_ALL,query="Select r from Referer r order by r.count desc"),
@NamedQuery(name=Referer.FIND_ALL_WITHOUT,query="Select r from Referer r where r.refererUri NOT LIKE :uri order by r.count desc")
})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Referer {
    public static final String PREFIX = "com.abien.xray.business.store.entity.Referer.";
    public static final String FIND_ALL = PREFIX + "findAllReferers";
    public static final String FIND_ALL_WITHOUT = PREFIX + "findAllReferersWithout";

    @Id
    private String refererUri;
    @Column(name="C_COUNTER")
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
