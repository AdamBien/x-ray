/*
 */
package com.abien.xray.business.grid.control;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.inject.Qualifier;

/**
 *
 * @author adam-bien.com
 */
@Qualifier
@Retention(RUNTIME)
@Target({METHOD, FIELD})
public @interface Daily {
}
