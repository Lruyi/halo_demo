package com.example.halo.demo.test;

import com.example.halo.demo.entity.Print;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description:   要求打印结果：12A34B56C.......5152Z
 * @Author: Halo_ry
 * @Date: 2020/3/29 14:48
 */
public class ThreadSwapTest {

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        Print print = new Print();
//        Thread thread1 = new Thread(print::print1);
//        Thread thread2 = new Thread(print::print2);
//        threadPool.submit(thread1);
//        threadPool.submit(thread2);
//        threadPool.shutdown();


        Runnable runnable1 = print::print1;
        Runnable runnable2 = print::print2;
        threadPool.submit(runnable1);
        threadPool.submit(runnable2);
        threadPool.shutdown();
    }
}
