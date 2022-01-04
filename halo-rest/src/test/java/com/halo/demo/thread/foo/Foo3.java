package com.halo.demo.thread.foo;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * @Description:  方法四： 阻塞队列
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
public class Foo3 {

    BlockingQueue<String> blockingDeque1, blockingDeque2;

    public Foo3() {
        //同步队列,没有容量，进去一个元素，必须等待取出来以后，才能再往里面放一个元素
        blockingDeque1 = new SynchronousQueue<>();
        blockingDeque2 = new SynchronousQueue<>();
    }

    public void first(Runnable printFirst) throws InterruptedException {
        // printFirst.run() outputs "first". Do not change or remove this line.
        printFirst.run();
        blockingDeque1.put("stop");
    }

    public void second(Runnable printSecond) throws InterruptedException {
        blockingDeque1.take();
        // printSecond.run() outputs "second". Do not change or remove this line.
        printSecond.run();
        blockingDeque2.put("stop");
    }

    public void third(Runnable printThird) throws InterruptedException {
        blockingDeque2.take();
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
