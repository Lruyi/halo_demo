package com.halo.demo.suanfa;

import java.util.Comparator;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2021/5/8 10:01
 */
public class Node {
    private static Comparator<Integer> cmp = (o1, o2) -> o2 - o1;

    public static void main(String[] args) {

//        TreeSet<Integer> treeSet = new TreeSet<>();
//        treeSet.add(4);
//        treeSet.add(10);
//        treeSet.add(5);
//        treeSet.add(8);
//        treeSet.add(3);
//        treeSet.add(1);
//        while (!treeSet.isEmpty()) {
//            System.out.println(treeSet.pollLast());
//        }

//        ConcurrentSkipListSet<Integer> set = new ConcurrentSkipListSet<>();
//        set.add(4);
//        set.add(10);
//        set.add(5);
//        set.add(8);
//        set.add(3);
//        set.add(1);
//        while (!set.isEmpty()) {
//            System.out.println(set.pollLast());
//        }

        ConcurrentSkipListMap<Integer, Object> map = new ConcurrentSkipListMap<>();
        map.put(4, 1);
        map.put(10, 1);
        map.put(5, 1);
        map.put(8, 1);
        map.put(3, 1);
        map.put(1, 1);
        while (!map.isEmpty()) {
            System.out.println(map.keySet().pollLast());
        }


//        Queue<Integer> queue = new PriorityBlockingQueue<>(11, cmp);
//        queue.add(4);
//        queue.add(10);
//        queue.add(5);
//        queue.add(8);
//        queue.add(3);
//        queue.add(1);
//        while (!queue.isEmpty()) {
//            System.out.println(queue.poll());
//        }

    }
}
