package com.example.halo.demo.test.proxy;

import com.example.halo.demo.entity.proxy.Bean1;
import com.example.halo.demo.entity.proxy.BeanInterface;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Description: JDK动态代理：使用jdk基于接口方式的动态代理
 * @Author: Halo_ry
 * @Date: 2020/4/20 10:26
 */
public class JDKProxy {
    public static void main(String[] args) {
        // 目标对象
        Bean1 bean1 = new Bean1();
        // 创建代理对象
        BeanInterface proxy = (BeanInterface) Proxy.newProxyInstance(bean1.getClass().getClassLoader(), bean1.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 在执行原方法之前做一些操作
                System.out.println("前置增强。。。");
                Object invoke = method.invoke(bean1, args);
                // 执行原方法之后做一些操作
                System.out.println("后置增强。。。");
                return invoke;
            }
        });
        //执行代理对象的方法
        proxy.show();
    }
}
