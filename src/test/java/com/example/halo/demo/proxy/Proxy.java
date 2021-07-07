package com.example.halo.demo.proxy;

/**
 * @Description: 代理类
 * @Author: Halo_ry
 * @Date: 2020/3/30 10:05
 */
public class Proxy implements Sourceable {

    private Source source;

    public Proxy() {
        this.source = new Source();
    }

    @Override
    public void method() {
        System.out.println("代理前。。。");
        source.method();
        System.out.println("代理后。。。");
    }
}
