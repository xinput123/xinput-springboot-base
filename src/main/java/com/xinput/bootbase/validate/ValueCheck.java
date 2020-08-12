package com.xinput.bootbase.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * @Author: xinput
 * @Date: 2020-06-29 11:40
 */
public class ValueCheck implements ConstraintValidator<Value, Object> {

    private String[] values;

    @Override
    public void initialize(Value value) {
        values = value.value();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null || value.toString().length() == 0) {
            return true;
        }
        return Arrays.asList(values).contains(value);
    }

}
