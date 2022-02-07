package com.halo.demo.test.treeset;

import java.util.Comparator;
import java.util.TreeSet;

/**
 * 需求：多线程场景：1. producer线程，往盒子里放球，每个球有一个数值
 *                   2. consumer线程，从盒子里面拿球，要求每次取最大值的球
 *
 *        解答：这里可以用到TreeSet，如果不传参数，默认不使用比较器，是自然排序（升序，从小到大），
 *             如果需要降序排序（从大到小）的话，需要先定义一个降序比较器，传入TreeSet有参构造器里面。
 *
 *       比较器生降序说明：
 *           private static Comparator<Object> cmp2 = new Comparator<Object>() {
 *                @Override
 *                public int compare(Object o1, Object o2) {
 *                    //升序（1 2 3 4 5）
 *                    return o1 - o2;
 *                    //降序（5 4 3 2 1）
 *                    return o2 - o1;
 *                }
 *            };
 *
 * @Description:    TreeSet 默认自然排序（升序）不需要传参，如果想要降序排序的话，我们可以自定义一个降序比较器（Comparator<T>）传入TreeSet的有参构造器里面
 *
 *                  pollFirst() // 获取第一个元素，并从TreeSet中删除
 *                  pollLast() // 获取最后一个元素，并从TreeSet中删除
 *
 * @Author: Halo_ry
 * @Date: 2020/4/24 14:41
 */
public class TreeSetTest {
    public static void main(String[] args) {
        // TreeSet 默认自然排序（升序）
        TreeSet<Integer> treeSet = new TreeSet<>();
        treeSet.add(3);
        treeSet.add(1);
        treeSet.add(8);
        treeSet.add(9);
        treeSet.add(5);
        while (!treeSet.isEmpty()) {
            System.out.print(treeSet.pollFirst() + " "); // 输出结果是：1 3 5 8 9
            System.out.print(treeSet.pollLast() + " "); // 输出结果是：9 8 5 3 1
        }

         // ---------------------------------------------

        // 把自定义的降序比较器传入构造器
        TreeSet<Integer> tree = new TreeSet<>(cmp);
        tree.add(3);
        tree.add(1);
        tree.add(8);
        tree.add(9);
        tree.add(5);
        while (!tree.isEmpty()) {
            System.out.print(tree.pollFirst() + " "); // 输出结果是：9 8 5 3 1
            System.out.print(tree.pollLast() + " "); // 输出结果是：1 3 5 8 9
        }
    }

    // 定义降序比较器
    private static Comparator<Integer> cmp = (o1, o2) -> (o2 - o1);
}
