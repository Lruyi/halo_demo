package com.example.halo.demo.strategy;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2020/3/30 11:42
 */
public class Plus extends AbstractCalculator implements ICalculator {
    @Override
    public int calculate(String exp) {
        int[] arrayInt = split(exp, "\\+");
        return arrayInt[0] + arrayInt[1];
    }
}
