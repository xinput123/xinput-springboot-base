package com.xinput.baseboot.annotation;

import com.xinput.bleach.util.StringUtils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标识出字段（尤其是：枚举常量）的含义
 *
 * @author zhaoxy
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Remark {
  String value() default StringUtils.EMPTY;
}
