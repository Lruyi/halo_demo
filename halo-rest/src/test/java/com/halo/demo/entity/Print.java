package com.halo.demo.entity;

/**
 * @Description:  要求打印结果：12A34B56C.......5152Z
 * @Author: Halo_ry
 * @Date: 2020/3/29 14:48
 */
public class Print {
    private boolean flag = false;
    public synchronized void print1() {
        for (int i = 0; i <= 25; i++) {
            if (flag) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.print(2 * i + 1 + "" + (2 * i + 2));
            flag = true;
            notifyAll();
        }
    }

    public synchronized void print2() {
        for (char i = 'A'; i <= 'Z'; i++) {
            if (!flag) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.print(i);
            flag = false;
            notifyAll();
        }
    }
}
