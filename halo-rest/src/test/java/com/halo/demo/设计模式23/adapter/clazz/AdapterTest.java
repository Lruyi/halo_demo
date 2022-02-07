package com.halo.demo.设计模式23.adapter.clazz;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2021/7/9 09:33
 */
public class AdapterTest {
    public static void main(String[] args) {
        // 这样Targetable接口的实现类就具有了Source类的功能
        Targetable target = new Adapter();
        target.method1();
        target.method2();
    }
}
