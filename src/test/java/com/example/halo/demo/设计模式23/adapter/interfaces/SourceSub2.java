package com.example.halo.demo.设计模式23.adapter.interfaces;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2021/7/9 09:53
 */
public class SourceSub2 extends Wrapper2 {

    @Override
    public void method2() {
        System.out.println("the sourceable interface's second Sub2!");
    }
}
