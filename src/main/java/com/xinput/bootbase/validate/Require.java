package com.xinput.bootbase.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 验证参数不能为空，且不允许为空字符串
 *
 * @Author: xinput
 * @Date: 2020-06-19 17:00
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = RequireCheck.class)
public @interface Require {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
