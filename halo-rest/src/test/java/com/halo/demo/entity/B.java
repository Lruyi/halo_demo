package com.halo.demo.entity;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2020/3/29 11:20
 */
public class B extends A {
    static {
        System.out.print("a");
    }

    public B() {
        System.out.print("b");
    }
}
