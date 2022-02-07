package com.halo.demo.test.priorityqueue;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

/**
 * 需求：多线程场景：1. producer线程，往盒子里放球，每个球有一个数值
 *                 2. consumer线程，从盒子里面拿球，要求每次取最大值的球
 *
 *      解答：这里可以用到优先队列 PriorityQueue，它是一个无限队列，默认容量11，如果不传参数，默认不使用比较器，是自然排序（升序，从小到大），
 *           如果需要降序排序（从大到小）的话，需要先定义一个降序比较器，传入PriorityQueue构造器里面。
 *
 *     比较器生降序说明：
 *         private static Comparator<Object> cmp2 = new Comparator<Object>() {
 *              @Override
 *              public int compare(Object o1, Object o2) {
 *                  //升序（1 2 3 4 5）
 *                  return o1 - o2;
 *                  //降序（5 4 3 2 1）
 *                  return o2 - o1;
 *              }
 *          };
 *
 *
 * @Description: 优先队列
 *
 *                  peek()//返回队首元素
 *                  poll()//返回队首元素，队首元素出队列
 *                  add()//添加元素
 *                  size()//返回队列元素个数
 *                  isEmpty()//判断队列是否为空，为空返回true,不空返回false
 *
 * @Author: Halo_ry
 * @Date: 2020/4/24 10:44
 */
public class PriorityQueueTest {
    public static void main(String[] args) throws InterruptedException {
        Queue<Integer> qq = new PriorityQueue<>(cmp);
        for (int i = 0; i < 10; i++) {
            int i1 = new Random().nextInt(10);
            System.out.println("随机数："+ i1);
            new Thread(() -> addQueue(qq, new Random().nextInt(10))).start();
        }
        Thread.sleep(50);
        new Thread(() -> getQueueMaxNum(qq)).start();


        //不用比较器，默认升序排列
//        Queue<Integer> queue = new PriorityQueue<>();
//        queue.add(3);
//        queue.add(1);
//        queue.add(8);
//        queue.add(9);
//        queue.add(5);
//        while (!queue.isEmpty()) {
//            System.out.print(queue.poll() + " ");
//        }
        // 输出结果是：1 3 5 8 9

        // --------------------------------------------

        // 把自定义的降序比较器传入构造器
//        Queue<Integer> qq = new PriorityQueue<>(cmp);
//        qq.add(3);
//        qq.add(1);
//        qq.add(8);
//        qq.add(9);
//        qq.add(5);
//        while (!qq.isEmpty()) {
//            System.out.print(qq.poll() + " ");
//        }
        // 输出结果是：9 8 5 3 1
    }

    private static void getQueueMaxNum(Queue<Integer> qq) {
        while (!qq.isEmpty()) {
            System.out.println("取出的最大球：" + qq.poll());
        }
    }

    private static void addQueue(Queue<Integer> qq, int num) {
        System.out.println("放进的球是：" + num + "号");
        qq.add(num);
    }

    //自定义比较器，降序排列
    private static Comparator<Integer> cmp = (o1, o2) -> o2 - o1;

    //自定义比较器，升序排列
    private static Comparator<Integer> cmp1 = Comparator.comparingInt(o -> o);
    private static Comparator<Integer> cmp2 = (o1, o2) -> o1 - o2;
}
