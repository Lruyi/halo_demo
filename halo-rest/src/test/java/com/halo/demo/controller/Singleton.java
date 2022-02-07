package com.halo.demo.controller;

/**
 * @Description: new一个对象有几个步骤。
 * 1.看class对象是否加载，如果没有就先加载class对象，
 * 2.分配内存空间，初始化实例，
 * 3.调用构造函数，
 * 4.返回地址给引用。
 * 而cpu为了优化程序，可能会进行指令重排序，打乱这3，4这几个步骤，导致实例内存还没分配，就被使用了。
 *
 * volatile防止指令重排序
 *
 * @Author: Halo_ry
 * @Date: 2020/3/27 23:45
 */
public class Singleton {
    private static volatile Singleton singleton;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (singleton == null) {
            synchronized (Singleton.class) {
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
}
