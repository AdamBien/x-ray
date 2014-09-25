/*
 */
package com.airhacks.xray.grid.control;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.inject.Qualifier;

/**
 *
 * @author adam-bien.com
 */
@Qualifier
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Grid {

    Name value();

    enum Name {

        HITS, REFERERS, TITLES, TRENDING, FIREHOSE, DIAGNOSTICS, DAILY, REJECTED, METHODS, EXCEPTIONS, FILTERS
    }
}
