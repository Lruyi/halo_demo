package com.example.halo.demo.设计模式23.decorator;

/**
 * @Description: 装饰者模式，需要把被装饰对象传入装饰者对象里面
 * @Author: Halo_ry
 * @Date: 2020/3/30 10:00
 */
public class DecoratorTest {
    public static void main(String[] args) {
        Sourceable source = new Source();
        Sourceable obj = new Decorator(source);
        obj.method();
    }
}
