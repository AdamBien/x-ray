/*
 */
package com.airhacks.satellite.cache.boundary;

import com.airhacks.xray.grid.control.GridInstance;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author adam-bien.com
 */
public class GridNameValidator implements ConstraintValidator<GridName, String> {

    @Override
    public void initialize(GridName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            new GridInstance(value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
