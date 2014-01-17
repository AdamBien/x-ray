package com.abien.xray.business.hits.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CacheValue {

    private String uri;
    private long count;

    public CacheValue() {
    }

    public CacheValue(String referer, String count) {
        this.uri = referer;
        this.count = Long.parseLong(count);
    }

    public String getRefererUri() {
        return uri;
    }

    public void setRefererUri(String refererUri) {
        this.uri = refererUri;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Referer{"
                + "refererUri='" + uri + '\''
                + ", count=" + count
                + '}';
    }
}
