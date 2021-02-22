package com.xinput.baseboot.validate;

import com.xinput.bleach.util.StringUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

/**
 * @author zanxus
 * @version 1.0.0
 * @description
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = RequireEmail.Check.class)
public @interface RequireEmail {

  String message();

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class Check implements ConstraintValidator<RequireEmail, Object> {

    static Pattern EMAIL_PATTERN = Pattern.compile("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[a-zA-Z0-9](?:[\\w-]*[\\w])?");

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
      if (value == null || StringUtils.isNullOrEmpty(String.valueOf(value))) {
        return false;
      }
      return EMAIL_PATTERN.matcher(value.toString()).matches();
    }
  }
}
