package com.halo.demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 拦截器添加参数
 *
 * @date : 2021/8/26 2:35 下午
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FeignAddParameter {

    Type type();
    
    String[] needSignParams() default {};
    
    Class<?> target() default Void.class;
    
    Algorithm algorithm() default Algorithm.NONE;
    
    /**
     * 自定义参数名，如果不指定则使用默认值
     */
    String paramName() default "";
    
    enum Type {
        HEADER,REQUEST_PARAM,REQUEST_BODY;
    }
    
    enum Algorithm {
        NONE, REFUND_UPPER,
    }

}
