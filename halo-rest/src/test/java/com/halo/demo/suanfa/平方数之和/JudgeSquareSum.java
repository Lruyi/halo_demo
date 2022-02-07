package com.halo.demo.suanfa.平方数之和;

/**
 * @Description: 平方数之和
 * 给定一个非负整数 c ，你要判断是否存在两个整数 a 和 b，使得 a2 + b2 = c 。
 *  示例 1：
 *
 * 输入：c = 5
 * 输出：true
 * 解释：1 * 1 + 2 * 2 = 5
 *
 * 示例 2：
 *
 * 输入：c = 3
 * 输出：false
 *
 * 示例 3：
 *
 * 输入：c = 4
 * 输出：true
 *
 * 示例 4：
 *
 * 输入：c = 2
 * 输出：true
 *
 * 示例 5：
 *
 * 输入：c = 1
 * 输出：true
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/sum-of-square-numbers
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 *
 * @Author: Halo_ry
 * @Date: 2021/5/7 19:34
 */
public class JudgeSquareSum {

    public static void main(String[] args) {
        int c = 136;

        boolean result = getJudgeSquareSum(c);
        System.out.println("输出：" + result);
    }

    private static boolean getJudgeSquareSum(int c) {
        int max = (int) Math.sqrt(c);
        int a = 0;
        while (a <= max) {
            int cc = a * a + max * max;
            if (cc == c) {
                return true;
            } else if (cc > c) {
                max--;
            } else {
                a++;
            }
        }

        return false;
    }
}
