package com.example.halo.demo.abstractfactory;

/**
 * @Description：
 * @Author RyLiu
 * @Date 2019/4/3
 */
public class SeniorTire implements ITire {
    @Override
    public void tire() {
        System.out.println("高级轮胎");
    }
}
