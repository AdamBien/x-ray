/*
 */
package com.abien.xray.business.grid.control;

import java.lang.annotation.Annotation;

/**
 *
 * @author adam-bien.com
 */
public class GridInstance implements Grid {

    private Name name;

    public GridInstance(Name name) {
        this.name = name;
    }

    public GridInstance(String name) {
        this(Name.valueOf(name));
    }

    @Override
    public Name value() {
        return this.name;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Grid.class;
    }

}
