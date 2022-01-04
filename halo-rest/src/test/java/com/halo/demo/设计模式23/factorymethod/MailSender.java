package com.halo.demo.设计模式23.factorymethod;

/**
 * @Description: 邮件产品
 * @Author: Halo_ry
 * @Date: 2020/3/31 18:04
 */
public class MailSender implements Sender {
    @Override
    public void send() {
        System.out.println("This is mail send...");
    }
}
