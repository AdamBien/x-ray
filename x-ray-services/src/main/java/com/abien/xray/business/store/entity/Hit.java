package com.abien.xray.business.store.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
@Entity
@NamedQueries({
@NamedQuery(name=Hit.FIND_ALL,query="Select h from Hit h where h.valid = true order by h.count desc"),
@NamedQuery(name=Hit.FIND_ALL_WITHOUT,query="Select h from Hit h where h.valid = true and h.actionId NOT LIKE :uri order by h.count desc"),
@NamedQuery(name=Hit.FIND_ALL_WITH,query="Select h from Hit h where h.valid = true and h.actionId LIKE :uri order by h.count desc")
})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Hit {

    public static final String PREFIX = "com.abien.xray.business.store.entity.Hit.";
    public static final String FIND_ALL = PREFIX + "findAllHits";
    public static final String FIND_ALL_WITHOUT = PREFIX + "findAllHitsWithout";
    public static final String FIND_ALL_WITH = PREFIX + "findAllHitsWith";


    @Id
    private String actionId;
    @Column(name="C_COUNTER")
    private long count;
    private boolean valid;
    
    public Hit() { /*...JPA*/}

    public Hit(String actionId, long count) {
        this.actionId = trimTrailingSlash(actionId);
        this.count = count;
        this.valid = true;
    }

    public String getActionId() {
        if(actionId == null){
            return null;
        }
        if(actionId.startsWith("/entry")){
            return trimTrailingSlash(actionId);
        }

        return actionId;
    }
  
    public final String trimTrailingSlash(String actionId) {
        if(actionId != null && actionId.endsWith("/")){
            return actionId.substring(0, actionId.length()-1);
        }
        return actionId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
            
    public boolean isValid() {
        return valid;
    }

    public void invalidate() {
        this.valid = false;
    }
}
