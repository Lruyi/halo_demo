package com.example.halo.demo.entity.account;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2020/4/1 14:26
 */
public class AddMoneyThread implements Runnable {
    private Account3 account;
    private BigDecimal money;

    public AddMoneyThread(Account3 account, BigDecimal money) {
        this.account = account;
        this.money = money;
    }

    @Override
    public void run() {
        synchronized (account) {
            account.deposit(money);
        }
    }


}
