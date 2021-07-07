package com.example.halo.demo.annotation;

import java.lang.annotation.*;

/**
 * @author Halo_ry
 * @date description:  上游机票单接口日志记录
 */
@Target(ElementType.METHOD)// 用在方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IfApiLogger {

    /**
     * 接口方法类型
     */
    int apiType();

    /**
     * 方法声明中customerName是第几个参数, 第一个参数索引为 0，默认是第二个参数，索引值为1
     */
    int customerNameIndex() default 1;
}
