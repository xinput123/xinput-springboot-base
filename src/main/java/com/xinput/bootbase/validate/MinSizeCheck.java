package com.xinput.bootbase.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author: xinput
 * @Date: 2020-06-29 11:55
 */
public class MinSizeCheck implements ConstraintValidator<MinSize, Object> {

    int minSize;

    @Override
    public void initialize(MinSize annotation) {
        minSize = annotation.value();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null || value.toString().length() == 0) {
            return true;
        }
        return value.toString().length() >= minSize;
    }
}
