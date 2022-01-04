package com.halo.demo.设计模式23.adapter.clazz;

/**
 * @Description: 适配器模式--被适配的类
 *
 *  核心思想就是：有一个Source类，拥有一个方法，待适配，目标接口是Targetable，通过Adapter类，将Source的功能扩展到Targetable里
 *
 * @author: halo_ry
 * @Date: 2021/7/9 09:25
 */
public class Source {

    public void method1() {
        System.out.println("this is original method!");
    }
}
