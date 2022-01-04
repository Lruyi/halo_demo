package com.halo.demo.entity.account;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2020/4/1 11:52
 */
public class Account2 {
    private BigDecimal balance;     // 账户余额
    private Lock accountLock = new ReentrantLock();

    public void deposit(BigDecimal money) {
        accountLock.lock();
        BigDecimal newBalance = balance == null ? BigDecimal.ZERO.add(money) : balance.add(money);
        try {
            try {
                Thread.sleep(10); // // 模拟此业务需要一段处理时间
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            balance = newBalance;
        } finally {
            accountLock.unlock();
        }
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
