package com.example.halo.demo.thread.foo;

import com.example.halo.demo.thread.foo.Foo1;

/**
 * @Description:  解法一：Synchronized锁和控制变量
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
public class Foo2 {
    //控制变量
    private int flag = 0;
    //定义Object对象为锁
    private Object lock = new Object();

    public Foo2() {
    }

    public void first(Runnable printFirst) throws InterruptedException {
        synchronized (lock) {
            //如果flag不为0则让first线程等待，while循环控制first线程如果不满住条件就一直在while代码块中，防止出现中途跳入，
            // 执行下面的代码，其余线程while循环同理
            while (flag != 0) {
                lock.wait();
            }
            // printFirst.run() outputs "first". Do not change or remove this line.
            printFirst.run();
            //定义成员变量为 1
            flag = 1;
            //唤醒其余所有的线程
            lock.notifyAll();
        }

    }

    public void second(Runnable printSecond) throws InterruptedException {
        synchronized (lock) {
            //如果成员变量不为1则让二号等待
            while (flag != 1) {
                lock.wait();
            }
            // printSecond.run() outputs "second". Do not change or remove this line.
            printSecond.run();
            //如果成员变量为 1 ，则代表first线程刚执行完，所以执行second，并且改变成员变量为 2
            flag = 2;
            //唤醒其余所有的线程
            lock.notifyAll();
        }
    }

    public void third(Runnable printThird) throws InterruptedException {
        synchronized (lock) {
            //如果flag不等于2 则一直处于等待的状态
            while (flag != 2) {
                lock.wait();
            }
            // printThird.run() outputs "third". Do not change or remove this line.
            printThird.run();
            //如果成员变量为 2 ，则代表second线程刚执行完，所以执行third，并且改变成员变量为 0
            flag = 0;
            lock.notifyAll();
        }
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
