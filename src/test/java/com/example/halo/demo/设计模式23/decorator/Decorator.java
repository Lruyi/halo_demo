package com.example.halo.demo.设计模式23.decorator;

/**
 * @Description: 装饰者
 *
 *  顾名思义，装饰模式就是给一个对象增加一些新的功能，而且是动态的，要求装饰对象和被装饰对象实现同一个接口，装饰对象持有被装饰对象的实例
 *
 * @Author: Halo_ry
 * @Date: 2020/3/30 9:59
 */
public class Decorator implements Sourceable {

    private Sourceable source;

    public Decorator(Sourceable source) {
        super();
        this.source = source;
    }

    @Override
    public void method() {
        System.out.println("装饰前。。。");
        source.method();
        System.out.println("装饰后。。。");
    }
}
