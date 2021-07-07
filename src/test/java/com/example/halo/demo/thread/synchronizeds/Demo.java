package com.example.halo.demo.thread.synchronizeds;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2021/5/28 9:26
 */
public class Demo {

    public Demo() {
    }

    private int number;

    public int getNumber() {
        return number;
    }

    public synchronized void addNumber() throws InterruptedException {
//        Thread.sleep(1000);
        number++;
        System.out.println(12121212);
//        Thread.sleep(1000);
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
