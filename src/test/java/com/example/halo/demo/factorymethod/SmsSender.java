package com.example.halo.demo.factorymethod;

/**
 * @Description: 短信产品
 * @Author: Halo_ry
 * @Date: 2020/3/31 18:05
 */
public class SmsSender implements Sender {
    @Override
    public void send() {
        System.out.println("This is sms send...");
    }
}
