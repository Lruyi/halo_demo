package com.example.halo.demo.abstractfactory;

/**
 * @Description：
 * @Author RyLiu
 * @Date 2019/4/3
 */
public class NormalBrake implements IBrake {
    @Override
    public void brake() {
        System.out.println("普通制动");
    }
}
