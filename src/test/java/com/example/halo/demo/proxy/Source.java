package com.example.halo.demo.proxy;

/**
 * @Description: 被代理类
 * @Author: Halo_ry
 * @Date: 2020/3/30 10:04
 */
public class Source implements Sourceable {

    @Override
    public void method() {
        System.out.println("原始方法。。。");
    }
}
