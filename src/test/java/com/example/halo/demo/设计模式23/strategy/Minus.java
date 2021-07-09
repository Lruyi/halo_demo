package com.example.halo.demo.设计模式23.strategy;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2020/3/30 11:44
 */
public class Minus extends AbstractCalculator implements ICalculator {
    @Override
    public int calculate(String exp) {
        int[] arrayInt = split(exp, "-");
        return arrayInt[0] - arrayInt[1];
    }
}
