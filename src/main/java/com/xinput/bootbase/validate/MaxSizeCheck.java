package com.xinput.bootbase.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author: xinput
 * @Date: 2020-06-29 11:55
 */
public class MaxSizeCheck implements ConstraintValidator<MaxSize, Object> {

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
