package com.halo.demo.设计模式23.adapter.clazz;

/**
 * @Description: 适配器类
 *
 *      Adapter类继承Source类，实现Targetable接口
 *
 * @author: halo_ry
 * @Date: 2021/7/9 09:27
 */
public class Adapter extends Source implements Targetable {

    @Override
    public void method2() {
        System.out.println("this is targetable method!");
    }
}
