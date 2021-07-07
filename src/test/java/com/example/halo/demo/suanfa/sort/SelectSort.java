package com.example.halo.demo.suanfati.sort;

/**
 * @Description: 选择排序
 * @Author: Halo_ry
 * @Date: 2021/4/28 9:40
 */
public class SelectSort {
    public static void main(String[] args) {
        int[] arr = {34, 12, 69, 24, 100, 21, 40};
        arr = selectSort(arr);

        getPrint(arr);
    }


    /**
     * 选择排序
     *
     * @param arr
     * @return
     */
    private static int[] selectSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    int tmp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = tmp;
                }
            }
        }
        return arr;
    }

    private static void getPrint(int[] arr) {
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
