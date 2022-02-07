package com.halo.demo.thread.foo;

import java.util.concurrent.Semaphore;

/**
 * @Description:  解法三：Semaphore（信号量）
 *      三个不同的线程 A、B、C 将会共用一个 Foo 实例。
 *          一个将会调用 first() 方法
 *          一个将会调用second() 方法
 *       还有一个将会调用 third() 方法
 * 请设计修改程序，以确保 second() 方法在 first() 方法之后被执行，third() 方法在 second() 方法之后被执行。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/print-in-order
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @Author: Halo_ry
 * @Date: 2021/3/9 19:36
 */
public class Foo1 {

    //声明两个 Semaphore变量
    private Semaphore spa, spb;

    public Foo1() {
        /**
         * 初始化Semaphore为0的原因：如果这个Semaphore为0，如果另一线程调用(acquire)这个Semaphore就会产生阻塞，
         * 便可以控制second和third线程的执行
         */
        spa = new Semaphore(0);
        spb = new Semaphore(0);


    }

    public void first(Runnable printFirst) throws InterruptedException {
        // printFirst.run() outputs "first". Do not change or remove this line.
        printFirst.run();
        //只有等first线程释放Semaphore后使Semaphore值为1,另外一个线程才可以调用（acquire）
        // 释放一个信号
        spa.release();
    }

    public void second(Runnable printSecond) throws InterruptedException {
        //只有spa为1才能执行acquire，如果为0就会产生阻塞
        // 获取一个信号
        spa.acquire();
        // printSecond.run() outputs "second". Do not change or remove this line.
        printSecond.run();
        //只有等second线程释放Semaphore后使Semaphore值为1,另外一个线程才可以调用（acquire）
        spb.release();
    }

    public void third(Runnable printThird) throws InterruptedException {
        //只有spb为1才能通过，如果为0就会阻塞
        spb.acquire();
        // printThird.run() outputs "third". Do not change or remove this line.
        printThird.run();
    }

    public static void main(String[] args) {
        Foo1 foo1 = new Foo1();
        Thread t1 = new Thread(()->{
            try {
                foo1.first(()-> System.out.println("first"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(()->{
            try {
                foo1.second(()-> System.out.println("second"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t3 = new Thread(()->{
            try {
                foo1.third(()-> System.out.println("third"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();
        t3.start();
    }
}
