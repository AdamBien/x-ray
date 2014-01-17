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
    private long epoch;

    public DailyHits(long hit) {
        this.hit = hit;
        this.epoch = System.currentTimeMillis();
    }

    public DailyHits(String date, String hit) {
        this.hit = Long.parseLong(hit);
        this.epoch = Long.parseLong(date);
    }

    public Date getDate() {
        return new Date(epoch);
    }

    public long getEpoch() {
        return this.epoch;
    }

    public long getHit() {
        return hit;
    }

    @Override
    public String toString() {
        return "DailyHits{" + "hit=" + hit + ", epoch=" + epoch + '}';
    }
}
