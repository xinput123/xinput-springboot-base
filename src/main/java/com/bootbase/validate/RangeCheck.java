package com.bootbase.validate;

import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.invoke.MethodHandles;

/**
 * @Author: xinput
 * @Date: 2020-06-20 18:21
 */
public class RangeCheck implements ConstraintValidator<Range, Object> {

    private static final Log LOG = LoggerFactory.make(MethodHandles.lookup());

    private double min;

    private double max;

    @Override
    public void initialize(Range constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
        validateParameters();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value instanceof String) {
            try {
                double v = Double.parseDouble(value.toString());
                return v >= min && v <= max;
            } catch (Exception e) {
                return false;
            }
        }

        if (value instanceof Number) {
            try {
                return ((Number) value).doubleValue() >= min && ((Number) value).doubleValue() <= max;
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    private void validateParameters() {
        if (max < min) {
            throw LOG.getLengthCannotBeNegativeException();
        }
    }
}
