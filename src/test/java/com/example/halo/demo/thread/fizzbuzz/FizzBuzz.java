package com.example.halo.demo.thread.fizzbuzz;

import java.util.concurrent.Semaphore;
import java.util.function.IntConsumer;

/**
 * @Description: 编写一个可以从 1 到 n 输出代表这个数字的字符串的程序，但是：
 * <p>
 * 如果这个数字可以被 3 整除，输出 "fizz"。
 * 如果这个数字可以被 5 整除，输出"buzz"。
 * 如果这个数字可以同时被 3 和 5 整除，输出 "fizzbuzz"。
 * 例如，当n = 15，输出：1, 2, fizz, 4, buzz, fizz, 7, 8, fizz, buzz, 11, fizz, 13, 14, fizzbuzz。
 * <p>
 * 假设有这么一个类：
 * <p>
 * class FizzBuzz {
 *  public FizzBuzz(int n) { ... }              // constructor
 *  public void fizz(printFizz) { ... }          // only output "fizz"
 *  public void buzz(printBuzz) { ... }          // only output "buzz"
 *  public void fizzbuzz(printFizzBuzz) { ... }  // only output "fizzbuzz"
 *  public void number(printNumber) { ... }      // only output the numbers
 * }
 * 请你实现一个有四个线程的多线程版FizzBuzz，同一个FizzBuzz实例会被如下四个线程使用：
 * <p>
 * 线程A将调用fizz()来判断是否能被 3 整除，如果可以，则输出fizz。
 * 线程B将调用buzz()来判断是否能被 5 整除，如果可以，则输出buzz。
 * 线程C将调用fizzbuzz()来判断是否同时能被 3 和 5 整除，如果可以，则输出fizzbuzz。
 * 线程D将调用number()来实现输出既不能被 3 整除也不能被 5 整除的数字。
 * <p>
 * <p>
 * 提示：
 * <p>
 * 本题已经提供了打印字符串的相关方法，如 printFizz() 等，具体方法名请参考答题模板中的注释部分。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/fizz-buzz-multithreaded
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * @Author: Halo_ry
 * @Date: 2021/4/14 22:34
 */
class FizzBuzz {
    private int n;
    private Semaphore fizz, buzz, fizzbuzz, number;

    public FizzBuzz(int n) {
        this.n = n;
        fizz = new Semaphore(0);
        buzz = new Semaphore(0);
        fizzbuzz = new Semaphore(0);
        number = new Semaphore(1);
    }

    // printFizz.run() outputs "fizz".
    public void fizz(Runnable printFizz) throws InterruptedException {
        for (int i = 3; i <= n; i += 3) {
            if (i % 5 != 0) {
                fizz.acquire();
                printFizz.run();
                number.release();
            }
        }
    }

    // printBuzz.run() outputs "buzz".
    public void buzz(Runnable printBuzz) throws InterruptedException {
        for (int i = 5; i <= n; i += 5) {
            if (i % 3 != 0) {
                buzz.acquire();
                printBuzz.run();
                number.release();
            }
        }
    }

    // printFizzBuzz.run() outputs "fizzbuzz".
    public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        for (int i = 15; i <= n; i += 15) {
            fizzbuzz.acquire();
            printFizzBuzz.run();
            number.release();
        }
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void number(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i++) {
            number.acquire();
            if (i % 3 == 0 && i % 5 == 0) {
                fizzbuzz.release();
            } else if (i % 3 == 0) {
                fizz.release();
            } else if (i % 5 == 0) {
                buzz.release();
            } else {
                printNumber.accept(i);
                number.release();
            }
        }
    }


    public static void main(String[] args) {
        FizzBuzz fizzBuzz = new FizzBuzz(15);

        new Thread(() -> {
            try {
                fizzBuzz.fizz(() -> System.out.println("fizz"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                fizzBuzz.buzz(() -> System.out.println("buzz"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                fizzBuzz.fizzbuzz(() -> System.out.println("fizzbuzz"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                fizzBuzz.number(System.out::println);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}