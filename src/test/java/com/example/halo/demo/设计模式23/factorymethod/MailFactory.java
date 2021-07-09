package com.example.halo.demo.设计模式23.factorymethod;

/**
 * @Description: 邮件工厂
 * @Author: Halo_ry
 * @Date: 2020/3/31 18:07
 */
public class MailFactory implements Provider {
    @Override
    public Sender produce() {
        return new MailSender();
    }
}
