package com.example.halo.demo.suanfati.happynumber;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 快乐数：
 *  快乐数（编写一个算法来判断一个数是不是“快乐数”。 一个“快乐数”定义为：对于一个正整数，每一次将该数替换为它每个位置上的数字的平方和，
 *  然后重复这个过程直到这个数变为 1，也可能是无限循环但始终变不到 1。如果可以变为 1， 那么这个数就是快乐数。）
 *
 *  示例：
 *      输入: 19
 *      输出: true
 * 解释:
 *      1^2 + 9^2 = 82
 *      8^2 + 2^2 = 68
 *      6^2 + 8^2 = 100
 *      1^2 + 0^2 + 0^2 = 1
 *
 *  思考：如果不是快乐数，如何停下来？   提示：如果求和后出现了重复的情况，那么，形成一个环形，永远听不下来，对于这样的数就是：不快乐数
 *
 *      不是快乐数：比如18
 *
 *      37  ---- 第一次出现
 *      58
 *      89
 *      145
 *      42
 *      20
 *      4
 *      16
 *      37  ---- 第二次出现
 *      58
 *      89
 *      145
 *      42
 *      20
 *      4
 *      16
 *      37  ---- 第三次出现
 *      ···
 *
 *      像这种情况就会出现无限死循环的情况，不是快乐数，但是停不下来，所以对于这种情况，我们应该判断，只要出现了重复的和，就返回false；
 *
 * @Author: Halo_ry
 * @Date: 2021/4/25 17:07
 */
public class HappyNumber1 {
    public static void main(String[] args) {
        int n = 19;
//        int n = 18;
        boolean result = isHappy(n);
        System.out.println("是否是快乐数：" + result);
    }

    private static boolean isHappy(int n) {
        List<Integer> list = new ArrayList<>();
        while (n != 1) {
            int sum = bitSquareSum(n);
            if (list.contains(sum)) {
                return false;
            }
            list.add(sum);
            n = sum;
        }
        return true;
    }

    /**
     * 求各个位上的数的平方和
     * @param n
     * @return
     */
    private static int bitSquareSum(int n) {
        int sum = 0;
        while (n > 0) {
            int i = n % 10;
            sum += i * i;
            n = n / 10;
        }
        return sum;
    }
}
