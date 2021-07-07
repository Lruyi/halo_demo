package com.example.halo.demo.abstractfactory;

/**
 * @Description：
 * @Author RyLiu
 * @Date 2019/4/3
 */
public class Test {
    public static void main(String[] args) {
        CarFactory factoryQ3 = new Q3Factory();
        factoryQ3.createBrake().brake();
        factoryQ3.createTire().tire();
        factoryQ3.createEngine().engine();
        System.out.println("----------------------------");
        CarFactory factoryM3 = new M3Factory();
        factoryM3.createBrake().brake();
        factoryM3.createTire().tire();
        factoryM3.createEngine().engine();
        System.out.println("----------------------------");
    }
}
