/*
 */
package com.abien.xray.business.store.boundary;

import java.lang.annotation.Annotation;

/**
 *
 * @author adam-bien.com
 */
public class CacheInstance implements Cache {

    private Name name;

    public CacheInstance(Name name) {
        this.name = name;
    }

    public CacheInstance(String name) {
        this(Name.valueOf(name));
    }

    @Override
    public Name value() {
        return this.name;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Cache.class;
    }

}
