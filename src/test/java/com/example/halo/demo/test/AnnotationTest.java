package com.example.halo.demo.test;

import com.example.halo.demo.MyAnnotation;
import com.example.halo.demo.设计模式23.proxy.ProxyTest;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2020/4/16 11:30
 */
@MyAnnotation("我是自定义注解。。。")
public class AnnotationTest {
    public static void main(String[] args) {
        Class<AnnotationTest> aClass = AnnotationTest.class;
        boolean annotationPresent = aClass.isAnnotationPresent(MyAnnotation.class);
        MyAnnotation annotation = aClass.getAnnotation(MyAnnotation.class);
        String value = annotation.value();
        System.out.println(annotationPresent);// true
        System.out.println(value);// 我是自定义注解。。。
    }
}
