package com.abien.xray.business.hits.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Hit {

    private String actionId;
    private long count;
    private boolean valid;

    public Hit() {
    }

    public Hit(String actionId, long count) {
        this.actionId = trimTrailingSlash(actionId);
        this.count = count;
        this.valid = true;
    }

    public String getActionId() {
        if (actionId == null) {
            return null;
        }
        if (actionId.startsWith("/entry")) {
            return trimTrailingSlash(actionId);
        }

        return actionId;
    }

    public final String trimTrailingSlash(String actionId) {
        if (actionId != null && actionId.endsWith("/")) {
            return actionId.substring(0, actionId.length() - 1);
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

    @Override
    public String toString() {
        return "Hit{" + "actionId=" + actionId + ", count=" + count + ", valid=" + valid + '}';
    }

}
