/*
 */
package com.airhacks.satellite.cache.boundary;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 *
 * @author adam-bien.com
 */
@Documented
@Constraint(validatedBy = GridNameValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface GridName {

    String message() default "Grid with the name {0} does not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
