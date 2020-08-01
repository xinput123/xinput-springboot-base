package com.bootbase.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @Author: xinput
 * @Date: 2020-06-29 11:55
 */
public class EmailCheck implements ConstraintValidator<Email, Object> {

    static Pattern emailPattern = Pattern.compile("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[a-zA-Z0-9](?:[\\w-]*[\\w])?");

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null || value.toString().length() == 0) {
            return true;
        }
        return emailPattern.matcher(value.toString()).matches();
    }
}
