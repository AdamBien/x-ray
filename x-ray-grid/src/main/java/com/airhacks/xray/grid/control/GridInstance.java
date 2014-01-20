/*
 */
package com.airhacks.xray.grid.control;

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

    public GridInstance(String stringValue) {
        this(Name.valueOf(stringValue.trim().toUpperCase()));
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
