package com.abien.xray.business.statistics.entity;

import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Adam Bien <blog.adam-bien.com>
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DailyHits {

    private long hit;
    private Date date;

    public DailyHits(long hit) {
        this.hit = hit;
    }

    public DailyHits(Date timestamp, long hit) {
        this(hit);
        this.date = timestamp;
    }

    public Date getDate() {
        return date;
    }

    public long getHit() {
        return hit;
    }

    @Override
    public String toString() {
        return "DailyHits{" + "hit=" + hit + ", date=" + date + '}';
    }
}
