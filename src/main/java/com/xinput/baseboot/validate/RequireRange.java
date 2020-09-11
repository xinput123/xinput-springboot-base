package com.xinput.baseboot.validate;

import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.invoke.MethodHandles;

/**
 * @author xinput
 * @date 2020-06-20 18:21
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = RequireRange.Check.class)
public @interface RequireRange {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return size the element must be higher or equal to
     */
    double min() default Double.MIN_VALUE;

    /**
     * @return size the element must be lower or equal to
     */
    double max() default Double.MAX_VALUE;

    class Check implements ConstraintValidator<RequireRange, Object> {

        private static final Log LOG = LoggerFactory.make(MethodHandles.lookup());

        private double min;

        private double max;

        @Override
        public void initialize(RequireRange constraintAnnotation) {
            min = constraintAnnotation.min();
            max = constraintAnnotation.max();
            validateParameters();
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            if (value == null) {
                return false;
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
}
