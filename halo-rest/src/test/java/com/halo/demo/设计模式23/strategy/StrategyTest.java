package com.halo.demo.设计模式23.strategy;

/**
 * @Description: 策略模式
 * @Author: Halo_ry
 * @Date: 2020/3/30 11:56
 */
public class StrategyTest {
    public static void main(String[] args) {
        String exp = "5+8";
        ICalculator iCalculator = new Plus();
        int result = iCalculator.calculate(exp);
        System.out.println("加法：" + result);

        //------------------------------------

        String exp1 = "5*8";
        ICalculator ic = new Multiply();
        int res = ic.calculate(exp1);
        System.out.println("乘法：" + res);
    }
}
