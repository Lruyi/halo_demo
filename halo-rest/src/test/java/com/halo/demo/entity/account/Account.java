package com.halo.demo.entity.account;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2020/4/1 11:30
 */
public class Account {
    private BigDecimal balance;     // 账户余额

    public synchronized void deposit(BigDecimal money) {
        BigDecimal newBalance = balance == null ? BigDecimal.ZERO.add(money) : balance.add(money);
        try {
            Thread.sleep(10); // // 模拟此业务需要一段处理时间
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        balance = newBalance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void sleep(String a) {
        System.out.println("晚安" + a);
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
