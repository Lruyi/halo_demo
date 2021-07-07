package com.example.halo.demo.abstractfactory;

/**
 * @Description： 抽象工厂
 * @Author RyLiu
 * @Date 2019/4/3
 */
public abstract class CarFactory {
    /**
     * 生产轮胎
     * @return
     */
    public abstract ITire createTire();

    /**
     * 生产发动机
     * @return
     */
    public abstract IEngine createEngine();

    /**
     * 生产制动系统
     * @return
     */
    public abstract IBrake createBrake();
}
