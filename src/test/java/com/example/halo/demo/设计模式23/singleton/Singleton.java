package com.example.halo.demo.设计模式23.singleton;

/**
 * @Description: 单例模式
 * @Author: Halo_ry
 * @Date: 2020/4/15 14:42
 */
public class Singleton {
    /** 私有构造方法，防止被实例化 */
    private Singleton() {}

    /** 此处使用一个内部类来维护单例 */
    private static class SingletonFactory {
        private static Singleton singleton = new Singleton();
    }

    public static Singleton getInstance() {
        return SingletonFactory.singleton;
    }
}
