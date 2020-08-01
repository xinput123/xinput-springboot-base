package com.bootbase.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Secure 注解类: 加到controller方法上表示该方法需要验证token。
 *
 * @Author: xinput
 * @Date: 2020-06-15 16:35
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Secure {
    boolean required() default true;
}
