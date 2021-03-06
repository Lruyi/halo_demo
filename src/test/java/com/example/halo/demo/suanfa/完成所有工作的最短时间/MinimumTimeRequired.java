package com.example.halo.demo.suanfa.完成所有工作的最短时间;

import java.util.Arrays;

/**
 * @Description:  完成所有工作的最短时间  (未完成 。。。。。。。。)
 *
 * 给你一个整数数组 jobs ，其中 jobs[i] 是完成第 i 项工作要花费的时间。
 *
 * 请你将这些工作分配给 k 位工人。所有工作都应该分配给工人，且每项工作只能分配给一位工人。工人的 工作时间 是完成分配给他们的所有工作花费时间的总和。请你设计一套最佳的工作分配方案，使工人的 最大工作时间 得以 最小化 。
 *
 * 返回分配方案中尽可能 最小 的 最大工作时间 。
 *
 * 示例 1：
 *
 * 输入：jobs = [3,2,3], k = 3
 * 输出：3
 * 解释：给每位工人分配一项工作，最大工作时间是 3 。
 *
 *
 * 示例 2：
 *
 * 输入：jobs = [1,2,4,7,8], k = 2
 * 输出：11
 * 解释：按下述方式分配工作：
 * 1 号工人：1、2、8（工作时间 = 1 + 2 + 8 = 11）
 * 2 号工人：4、7（工作时间 = 4 + 7 = 11）
 * 最大工作时间是 11 。
 *
 *
 * 提示：
 *
 * 1 <= k <= jobs.length <= 12
 * 1 <= jobs[i] <= 10^7
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/find-minimum-time-to-finish-all-jobs
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @Author: Halo_ry
 * @Date: 2021/5/8 11:50
 */
public class MinimumTimeRequired {

    public static void main(String[] args) {
//        int[] jobs = {1,3,5,1000};
        int[] jobs = {12,13,14,17,25};
        int k = 3;
        int result = getMinimumTimeRequired(jobs, k);
        System.out.println("输出：" + result);
    }

    private static int getMinimumTimeRequired(int[] jobs, int k) {
        int sum = Arrays.stream(jobs).sum();
        int max = Arrays.stream(jobs).reduce(Integer::max).orElse(0);
        int maxFlag = max;
        boolean flag = false;
        while (k <= jobs.length) {
            if (k * maxFlag >= sum) {
                if (flag) {
                    if (maxFlag < max) {
                        return max;
                    }
                    return maxFlag;
                }
                if (k * maxFlag >= sum) {
                    maxFlag--;
                }
            } else {
                flag = true;
                maxFlag++;
            }
        }
        return 0;
    }
}
