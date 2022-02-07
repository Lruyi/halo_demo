package com.halo.demo.设计模式23.adapter.object;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2021/7/9 09:33
 */
public class AdapterTest {
    public static void main(String[] args) {
        Source source = new Source();
        Targetable target = new Wrapper(source);
        target.method1();
        target.method2();
    }
}
