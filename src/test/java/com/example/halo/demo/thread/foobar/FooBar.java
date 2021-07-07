package com.example.halo.demo.thread.foobar;

import java.util.concurrent.Semaphore;

/**
 * @Description:
 * class FooBar {
 *      public void foo() {
 *          for (int i = 0; i < n; i++) {
 *              print("foo");
 *          }
 *      }
 *
 *      public void bar() {
 *          for (int i = 0; i < n; i++) {
 *              print("bar");
 *          }
 *      }
 * }
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/print-foobar-alternately
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * @Author: Halo_ry
 * @Date: 2021/3/16 23:22
 */
public class FooBar {
    private int n;
    private Semaphore spa, spb;

    public FooBar(int n) {
        this.n = n;
        spa = new Semaphore(1);
        spb = new Semaphore(0);
    }

    public void foo(Runnable printFoo) throws InterruptedException {

        for (int i = 0; i < n; i++) {
            spa.acquire();
            // printFoo.run() outputs "foo". Do not change or remove this line.
            printFoo.run();
            spb.release();
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {

        for (int i = 0; i < n; i++) {
            spb.acquire();
            // printBar.run() outputs "bar". Do not change or remove this line.
            printBar.run();
            spa.release();
        }
    }


    public static void main(String[] args) {
        FooBar fooBar = new FooBar(5);
        Thread t1 = new Thread(() -> {
            try {
                fooBar.foo(() -> System.out.print("foo"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                fooBar.bar(() -> System.out.print("bar"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();
    }


}
