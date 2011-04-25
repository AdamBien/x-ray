package com.abien.xray.business.useragent.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.StringTokenizer;

/**
 * User: blog.adam-bien.com
 * Date: 07.01.11
 * Time: 13:12
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@NamedQuery(name=UserAgent.FIND_ALL,query="Select u from UserAgent u order by u.visitCount desc")
public class UserAgent {

    public static final String PREFIX = "com.abien.xray.business.useragent.entity.UserAgent.";
    public static final String FIND_ALL = PREFIX + "findAll";

    enum KnownBrowsers{
        FIREFOX, MSIE, OPERA, SAFARI,FLOCK

    }

    @Id
    private String userAgentName;
    private long visitCount;

    public UserAgent(String userAgentName) {
        this.userAgentName = normalize(userAgentName);
        this.visitCount = 1;
    }

    String normalize(String userAgentName) {
        String userAgent = null;
        if(userAgentName == null){
            throw new IllegalArgumentException("User-Agent cannot be null!");
        }
        KnownBrowsers[] knownBrowsers = KnownBrowsers.values();
        for (KnownBrowsers knownBrowser : knownBrowsers) {
            if(userAgentName.toUpperCase().contains(knownBrowser.name())){
                return knownBrowser.name();
            }
        }

        if(userAgent.length() > 20){
            userAgent = userAgent.substring(0,20);
        }
        return userAgent;
    }


    public UserAgent() { }

    public void increaseCounter(){
        visitCount++;
    }

    public String getUserAgentName() {
        return userAgentName;
    }

    public long getVisitCount() {
        return visitCount;
    }
}

