package com.example.halo.demo.abstractfactory;

/**
 * @Description：
 * @Author RyLiu
 * @Date 2019/4/3
 */
public class M3Factory extends CarFactory {
    @Override
    public ITire createTire() {
        return new SeniorTire();
    }

    @Override
    public IEngine createEngine() {
        return new NormalEngine();
    }

    @Override
    public IBrake createBrake() {
        return new SeniorBrake();
    }
}
