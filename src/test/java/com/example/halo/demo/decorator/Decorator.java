package com.example.halo.demo.decorator;

/**
 * @Description: 装饰者
 * @Author: Halo_ry
 * @Date: 2020/3/30 9:59
 */
public class Decorator implements Sourceable {

    private Sourceable source;

    public Decorator(Sourceable source) {
        this.source = source;
    }

    @Override
    public void method() {
        System.out.println("装饰前。。。");
        source.method();
        System.out.println("装饰后。。。");
    }
}
