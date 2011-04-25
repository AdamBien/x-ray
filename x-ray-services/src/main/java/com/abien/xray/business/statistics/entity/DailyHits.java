package com.abien.xray.business.statistics.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Adam Bien <blog.adam-bien.com>
 */
@Entity
@NamedQueries({
    @NamedQuery(name=DailyHits.findAllDescending,query="Select d from DailyHits d order by d.date desc")
})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DailyHits{
    
    @Id
    @GeneratedValue
    @XmlTransient
    private long id;

    private long hit;

    @Temporal(TemporalType.DATE)
    private Date date;
    
    public final static String PREFIX = "com.abien.xray.business.statistics.entiy.DailyHits";
    public final static String findAllDescending = PREFIX + "findAllDescending";

    public DailyHits() { /* required by jpa */}

    public DailyHits(long hit) {
        this.hit = hit;
    }
    
    public DailyHits(Date timestamp,long hit) {
        this(hit);
        this.date = timestamp;
    }

    public Date getDate() {
        return date;
    }

    public long getId() {
        return id;
    }

    public long getHit() {
        return hit;
    }

    @Override
    public String toString() {
        return "DailyHits{" + "id=" + id + ", date=" + date + ", hit=" + getHit() +'}';
    }


}
