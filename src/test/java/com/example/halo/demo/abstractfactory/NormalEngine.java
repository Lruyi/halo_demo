package com.example.halo.demo.abstractfactory;

/**
 * @Description：
 * @Author RyLiu
 * @Date 2019/4/3
 */
public class NormalEngine implements IEngine {
    @Override
    public void engine() {
        System.out.println("普通发动机");
    }
}
