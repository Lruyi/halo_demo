package com.example.halo.demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2020/4/16 11:19
 */
@Target({ElementType.TYPE, ElementType.METHOD})// 作用在类上和方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotation {
    public abstract String value();
}
