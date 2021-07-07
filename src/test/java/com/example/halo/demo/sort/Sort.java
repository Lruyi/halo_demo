package com.example.halo.demo.sort;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

/**
 * @Description: 排序
 * @Author: Halo_ry
 * @Date: 2020/4/20 14:49
 */
public class Sort {
    @Test
    public void test() {
        int[] arr = {34, 12, 69, 24, 100, 21, 40};
        // 选择排序
//        arr = selectSort(arr);
        // 冒泡排序
//        arr = bubbleSort(arr);
        // 快速排序
        quickSort(arr, 0, arr.length - 1);
//        quick_Sort(arr, 0, arr.length - 1);
        print(arr);
    }

    /**
     * 快速排序
     *
     * @param arr 数组
     * @param low 低位
     * @param high 高位
     */
    private void quickSort(int[] arr, int low, int high) {
        // 找寻基准数据的正确索引
        int index = getIndex(arr, low, high);
        // 进行迭代对index之前和之后的数组进行相同的操作使整个数组变成有序
        quickSort(arr, 0, index - 1);
        quickSort(arr, index + 1, high);
    }

    private static int getIndex(int[] arr, int low, int high) {
        // 基准数据
        int tmp = arr[low];
        while (low < high) {
            // 当队尾的元素大于等于基准数据时,向前挪动high指针
            while (low < high && arr[high] >= tmp) {
                high--;
            }
            // 如果队尾元素小于tmp了,需要将其赋值给low
            arr[low] = arr[high];
            // 当队首元素小于等于tmp时,向前挪动low指针
            while (low < high && arr[low] <= tmp) {
                low++;
            }
            // 当队首元素大于tmp时,需要将其赋值给high
            arr[high] = arr[low];
        }
        // 跳出循环时low和high相等,此时的low或high就是tmp的正确索引位置
        // 由原理部分可以很清楚的知道low位置的值并不是tmp,所以需要将tmp赋值给arr[low]
        arr[low] = tmp;
        return low; // 返回tmp的正确位置
    }

    /**
     * 快速排序
     *
     * @param arr
     * @param begin
     * @param end
     */
    void quick_Sort(int[] arr, int begin, int end) {
        if (begin > end) {
            return;
        }
        int tmp = arr[begin];
        int i = begin;
        int j = end;
        while (i != j) {
            while (arr[j] >= tmp && j > i) {
                j--;
            }
            while (arr[i] <= tmp && j > i) {
                i++;
            }
            if (j > i) {
                int t = arr[i];
                arr[i] = arr[j];
                arr[j] = t;
            }
        }
        arr[begin] = arr[i];
        arr[i] = tmp;
        quick_Sort(arr, begin, i - 1);
        quick_Sort(arr, i + 1, end);
    }


    /**
     * 冒泡排序:（先找出最大的放到最后面）
     * 第一次循环先把最大的数字找出来放到最后一位，
     * 第二次循环会把第二大的数字放到倒数第二位，
     * ... ...
     * <p>
     * 原始：{34, 12, 69, 24, 100, 21, 40}
     * <p>
     * 第1次循环 [12,34,24,69,21,40,100]
     * 第2次循环 [12,24,34,21,40,69,100]
     * 第3次循环 [12,24,21,34,40,69,100]
     * 第4次循环 [12,21,24,34,40,69,100]
     * 第5次循环 [12,21,24,34,40,69,100]
     * 第6次循环 [12,21,24,34,40,69,100]
     *
     * @param arr
     * @return
     */
    private int[] bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
            System.out.println("第" + (i + 1) + "次循环 " + JSON.toJSONString(arr));
        }
        return arr;
    }

    /**
     * 选择排序: （先找出最小的，放到最前面）
     * 第一次循环会把最小的找出来放到第一位，以此类推
     * <p>
     * 原始：{34, 12, 69, 24, 100, 21, 40}
     * <p>
     * 第1次循环 [12,34,69,24,100,21,40]
     * 第2次循环 [12,21,69,34,100,24,40]
     * 第3次循环 [12,21,24,69,100,34,40]
     * 第4次循环 [12,21,24,34,100,69,40]
     * 第5次循环 [12,21,24,34,40,100,69]
     * 第6次循环 [12,21,24,34,40,69,100]
     *
     * @param arr
     * @return
     */
    private int[] selectSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
            System.out.println("第" + (i + 1) + "次循环 " + JSON.toJSONString(arr));
        }
        return arr;
    }

    /**
     * 打印
     *
     * @param arr
     */
    private void print(int[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            if (i < arr.length - 1) {
                System.out.print(arr[i] + ", ");
            } else {
                System.out.print(arr[i] + "]");
            }
        }
    }
}
