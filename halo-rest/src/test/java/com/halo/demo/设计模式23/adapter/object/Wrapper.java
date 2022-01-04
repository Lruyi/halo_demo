package com.halo.demo.设计模式23.adapter.object;

/**
 * @Description: 适配器
 * @author: halo_ry
 * @Date: 2021/7/9 09:38
 */
public class Wrapper implements Targetable {

    private Source source;

    public Wrapper(Source source) {
        super();
        this.source = source;
    }

    @Override
    public void method1() {
        source.method1();
    }

    @Override
    public void method2() {
        System.out.println("this is targetable method!");
    }
}
