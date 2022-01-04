package com.halo.demo.设计模式23.factorymethod;

/**
 * @Description: 工厂方法模式
 *          一个抽象产品类，可以派生出多个具体产品类。
 *          一个抽象工厂类，可以派生出多个具体工厂类。
 *          每个具体工厂类只能创建一个具体产品类的实例。
 *
 * @Author: Halo_ry
 * @Date: 2020/3/31 18:10
 */
public class FactoryMethodTest {
    public static void main(String[] args) {
        // 发送邮件
        Provider provider = new MailFactory();
        Sender sender = provider.produce();
        sender.send();

        // 发送短信
        Provider provider1 = new SmsFactory();
        Sender sender1 = provider1.produce();
        sender1.send();

    }
}
