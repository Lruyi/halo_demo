package com.example.halo.demo.设计模式23.adapter.object;

/**
 * @Description: 适配器模式--被适配的类
 *
 *  基本思路和类的适配器模式相同，只是将Adapter类作修改，这次不继承Source类，而是持有Source类的实
 *
 * @author: halo_ry
 * @Date: 2021/7/9 09:25
 */
public class Source {

    public void method1() {
        System.out.println("this is original method!");
    }
}
