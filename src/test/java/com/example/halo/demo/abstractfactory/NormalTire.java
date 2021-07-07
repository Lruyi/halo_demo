package com.example.halo.demo.abstractfactory;

/**
 * @Description：
 * @Author RyLiu
 * @Date 2019/4/3
 */
public class NormalTire implements ITire {
    @Override
    public void tire() {
        System.out.println("普通轮胎");
    }
}
