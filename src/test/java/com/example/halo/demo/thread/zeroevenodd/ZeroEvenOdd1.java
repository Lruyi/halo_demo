package com.example.halo.demo.thread.zeroevenodd;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.function.IntConsumer;

/**
 * @Description:
 *
 *  假设有这么一个类：
 *  class ZeroEvenOdd {
 *      public ZeroEvenOdd(int n) { ... }    // 构造函数
 *      public void zero(printNumber) { ... }  // 仅打印出 0
 *      public void even(printNumber) { ... }  // 仅打印出 偶数
 *      public void odd(printNumber) { ... }   // 仅打印出 奇数
 *  }
 *
 *  相同的一个ZeroEvenOdd类实例将会传递给三个不同的线程：
 *
 * 线程 A 将调用zero()，它只输出 0 。
 * 线程 B 将调用even()，它只输出偶数。
 * 线程 C 将调用odd()，它只输出奇数。
 * 每个线程都有一个printNumber 方法来输出一个整数。请修改给出的代码以输出整数序列010203040506... ，其中序列的长度必须为 2n。
 *
 *  示例 1：
 *
 *      输入：n = 2
 *      输出："0102"
 *      说明：三条线程异步执行，其中一个调用 zero()，另一个线程调用 even()，最后一个线程调用odd()。正确的输出为 "0102"。
 * 示例 2：
 *
 *      输入：n = 5
 *      输出："0102030405"
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/print-zero-even-odd
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @Author: Halo_ry
 * @Date: 2021/3/23 22:07
 */
public class ZeroEvenOdd1 {

    private int n;

    private Semaphore zero, even, odd;

    public ZeroEvenOdd1(int n) {
        this.n = n;
        zero = new Semaphore(1);
        even = new Semaphore(0);
        odd = new Semaphore(0);
    }

    /**
     * 它只输出 0
     * @param printNumber
     * @throws InterruptedException
     */
    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i++) {
            zero.acquire();
            printNumber.accept(0);
            if (i % 2 == 0) {
                even.release();
            } else {
                odd.release();
            }
        }
    }

    /**
     * 它只输出偶数。
     * @param printNumber
     * @throws InterruptedException
     */
    public void even(IntConsumer printNumber) throws InterruptedException {
        for (int i = 2; i <= n; i+=2) {
            even.acquire();
            printNumber.accept(i);
            zero.release();
        }
    }

    /**
     * 它只输出奇数
     * @param printNumber
     * @throws InterruptedException
     */
    public void odd(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i+=2) {
            odd.acquire();
            printNumber.accept(i);
            zero.release();
        }
    }

    public static void main(String[] args) {
        ZeroEvenOdd1 zeo = new ZeroEvenOdd1(5);
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
            Runnable runnable1 = () -> {
                try {
                    zeo.zero(System.out::print);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };

            Runnable runnable2 = () -> {
                try {
                    zeo.even(System.out::print);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };

            Runnable runnable3 = () -> {
                try {
                    zeo.odd(System.out::print);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };

            threadPool.execute(runnable1);
            threadPool.execute(runnable2);
            threadPool.execute(runnable3);


    }
}
