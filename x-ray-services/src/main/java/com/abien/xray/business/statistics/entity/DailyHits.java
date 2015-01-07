package com.abien.xray.business.statistics.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private LocalDate date;

    public DailyHits(long hit) {
        this.hit = hit;
        this.date = LocalDate.now();
    }

    public DailyHits(String date, String hit) {
        this.date = LocalDate.parse(date);
        this.hit = Long.parseLong(hit);
    }

    public String getDateAsString() {
        return date.format(DateTimeFormatter.ISO_DATE);
    }

    public LocalDate getDate() {
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
