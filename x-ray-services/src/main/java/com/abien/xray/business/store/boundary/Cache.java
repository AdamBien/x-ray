/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abien.xray.business.store.boundary;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.inject.Qualifier;

/**
 *
 * @author adam bien, adam-bien.com
 */
@Qualifier
@Retention(RUNTIME)
@Target({FIELD,METHOD})
public @interface Cache {
}
