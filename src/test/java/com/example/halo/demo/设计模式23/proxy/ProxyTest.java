package com.example.halo.demo.设计模式23.proxy;

/**
 * @Description: 代理模式，不要把被代理类传入代理对象里面，而是在代理对象的空参构造里面创建一个被代理对象
 * @Author: Halo_ry
 * @Date: 2020/3/30 10:07
 */
public class ProxyTest {
    public static void main(String[] args) {
        Sourceable source = new Proxy();
        source.method();
    }
}
