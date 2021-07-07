package com.example.halo.demo.factorymethod;

/**
 * @Description: 短信工厂
 * @Author: Halo_ry
 * @Date: 2020/3/31 18:08
 */
public class SmsFactory implements Provider {
    @Override
    public Sender produce() {
        return new SmsSender();
    }
}
