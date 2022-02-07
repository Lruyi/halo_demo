package com.halo.demo.suanfa.两数之和;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 两数之和
 * 给定一个整数数组 nums和一个整数目标值 target，请你在该数组中找出 和为目标值 的那两个整数，并返回它们的数组下标。
 * <p>
 * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。
 * <p>
 * 你可以按任意顺序返回答案。
 * <p>
 * 示例 1：
 * <p>
 * 输入：nums = [2,7,11,15], target = 9
 * 输出：[0,1]
 * 解释：因为 nums[0] + nums[1] == 9 ，返回 [0, 1] 。
 * 示例 2：
 * <p>
 * 输入：nums = [3,2,4], target = 6
 * 输出：[1,2]
 * 示例 3：
 * <p>
 * 输入：nums = [3,3], target = 6
 * 输出：[0,1]
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/two-sum
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * @Author: Halo_ry
 * @Date: 2021/4/27 19:56
 */
public class TwoSum {

    public static void main(String[] args) {
        int[] nums = {2, 7, 11, 15};
        int target = 9;

//        int[] result = getIndex(nums, target);
        int[] result1 = getIndex1(nums, target);
//        System.out.println("返回结果：[" + result[0] + "," + result[1] + "]");
        System.out.println("返回结果：[" + result1[0] + "," + result1[1] + "]");
    }

    private static int[] getIndex(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i])) {
                Integer j = map.get(target - nums[i]);
                return new int[]{j, i};
            }
            map.put(nums[i], i);
        }

        return new int[0];
    }


    private static int[] getIndex1(int[] nums, int target) {
        /**
         * 类似冒泡排序
         */
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = 0; j < nums.length - 1 - i; j++) {
                if (nums[j] + nums[j + 1] == target) {
                    return new int[]{j, j + 1};
                }
            }
        }

        /**
         * 类似选择排序
         */
//        for (int i = 0; i < nums.length - 1; i++) {
//            for (int j = i + 1; j < nums.length; j++) {
//                if (nums[i] + nums[j] == target) {
//                    return new int[]{i,j};
//                }
//            }
//        }
        return new int[0];
    }
}
