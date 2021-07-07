package com.example.halo.demo.abstractfactory;

/**
 * @Description：
 * @Author RyLiu
 * @Date 2019/4/3
 */
public class SeniorBrake implements IBrake {
    @Override
    public void brake() {
        System.out.println("高级制动");
    }
}
