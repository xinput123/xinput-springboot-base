package com.xinput.bootbase.validate;

import org.apache.commons.collections.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

/**
 * @Author: xinput
 * @Date: 2020-06-19 18:27
 */
public class RequireCheck implements ConstraintValidator<Require, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        if (value instanceof String) {
            return String.valueOf(value).trim().length() > 0;
        }

        if (value instanceof Collection<?>) {
            return CollectionUtils.isNotEmpty(((Collection<?>) value));
//            return ((Collection<?>) value).size() > 0;
        }

        if (value.getClass().isArray()) {
            try {
                return java.lang.reflect.Array.getLength(value) > 0;
            } catch (Exception e) {
                return false;
            }
        }

        return true;
    }
}
