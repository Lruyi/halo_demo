package com.halo.demo.test.proxy;

import com.halo.demo.entity.proxy.Bean2;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Description: CGLIB: 为目标对象生成子对象的方法 完成代理
 * @Author: Halo_ry
 * @Date: 2020/4/20 10:44
 */
public class CGlibProxy {
    public static void main(String[] args) {
        // 创建目标对象
        Bean2 bean2 = new Bean2();
        //1、创建增强器
        Enhancer enhancer = new Enhancer();
        //2、设置父类是谁
        enhancer.setSuperclass(Bean2.class);
        //3、设置回调方法
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("前置增强........");
                Object invoke = method.invoke(bean2, objects);
                System.out.println("后置增强........");
                return invoke;
            }
        });
        //4、创建基于设置的父类的代理对象
        Bean2 proxy = (Bean2) enhancer.create();
        proxy.show();
    }
}
