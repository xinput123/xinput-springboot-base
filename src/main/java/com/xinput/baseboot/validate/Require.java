package com.xinput.baseboot.validate;

import org.apache.commons.collections4.CollectionUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

/**
 * 验证参数不能为空，且不允许为空字符串
 *
 * @author xinput
 * @date 2020-06-19 17:00
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = Require.Check.class)
public @interface Require {

  String message();

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class Check implements ConstraintValidator<Require, Object> {

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
}
