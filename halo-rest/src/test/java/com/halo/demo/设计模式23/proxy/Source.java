package com.halo.demo.设计模式23.proxy;

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
