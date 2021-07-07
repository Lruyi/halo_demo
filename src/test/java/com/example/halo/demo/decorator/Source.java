package com.example.halo.demo.decorator;

/**
 * @Description: 被装饰者
 * @Author: Halo_ry
 * @Date: 2020/3/30 9:58
 */
public class Source implements Sourceable {
    @Override
    public void method() {
        System.out.println("原始方法");
    }
}
