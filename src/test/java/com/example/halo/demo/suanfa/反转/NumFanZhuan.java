package com.example.halo.demo.suanfati.反转;

/**
 * @Description:
 * 给你一个 32 位的有符号整数 x ，返回将 x 中的数字部分反转后的结果。
 *
 * 如果反转后整数超过 32 位的有符号整数的范围[−2^31, 2^31 − 1] ，就返回 0。
 *
 * 假设环境不允许存储 64 位整数（有符号或无符号）。
 *
 * 示例 1：
 *
 * 输入：x = 123
 * 输出：321
 *
 * 示例 2：
 *
 * 输入：x = -123
 * 输出：-321
 *
 * 示例 3：
 *
 * 输入：x = 120
 * 输出：21
 *
 * 示例 4：
 *
 * 输入：x = 0
 * 输出：0
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/reverse-integer
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @Author: Halo_ry
 * @Date: 2021/4/27 22:08
 */
public class NumFanZhuan {
    public static void main(String[] args) {
//        int x = 1534236469;
        int x = 15342364;
        int result = numReverse(x);
        System.out.println("反转：" + result);
    }

    private static int numReverse(int x) {
        int s = 0;
        while (x != 0) {
            int n = x % 10;
            x /= 10;
            // 2^31 - 1 个位数是7，2^-31 个位数是8
            if (s > Integer.MAX_VALUE/10 || (s == Integer.MAX_VALUE / 10 && n > 7)) return 0;
            if (s < Integer.MIN_VALUE/10 || (s == Integer.MIN_VALUE / 10 && n < -8)) return 0;

            s = s * 10 + n;
        }
        return s;
    }
}
