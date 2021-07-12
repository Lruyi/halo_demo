package com.example.halo.demo.sort;

/**
 * @Description: 冒泡排序
 * @Author: Halo_ry
 * @Date: 2021/5/6 19:34
 */
public class BubbleSort {
    public static void main(String[] args) {
        int[] arr = {34, 12, 69, 24, 100, 21, 40};

        bubbleSort(arr);

        getPrint(arr);
    }

    /**
     * 冒泡排序
     * @param arr
     */
    private static void bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                }
            }
        }
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
