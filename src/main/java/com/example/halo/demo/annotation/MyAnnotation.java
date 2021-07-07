package com.example.halo.demo.annotation;

import java.lang.annotation.*;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2020/4/16 11:19
 */
@Target({ElementType.TYPE, ElementType.METHOD})// 作用在类上和方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyAnnotation {
    public abstract String value();
}
