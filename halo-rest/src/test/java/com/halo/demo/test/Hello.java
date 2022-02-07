package com.halo.demo.test;

import com.halo.demo.entity.A;
import com.halo.demo.entity.B;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2020/3/29 11:20
 */
public class Hello {
    public static void main(String[] args) {
        A ab = new B(); // 1a2b
        ab = new B(); // 2b
        // 整体输出结果：1a2b2b
    }
}
