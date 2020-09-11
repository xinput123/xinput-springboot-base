package com.xinput.baseboot.validate;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zanxus
 * @version 1.0.0
 * @date 2018-04-18 14:57
 * @description
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = MaxSize.Check.class)
public @interface MaxSize {

    int value();

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Check implements ConstraintValidator<MaxSize, Object> {

        int maxSize;

        @Override
        public void initialize(MaxSize annotation) {
            maxSize = annotation.value();
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            if (value == null || value.toString().length() == 0) {
                return true;
            }
            return value.toString().length() <= maxSize;
        }
    }
}
