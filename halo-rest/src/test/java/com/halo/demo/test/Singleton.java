package com.halo.demo.test;

/**
 * @Description: 单例
 * @Author: Halo_ry
 * @Date: 2020/3/27 17:56
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
