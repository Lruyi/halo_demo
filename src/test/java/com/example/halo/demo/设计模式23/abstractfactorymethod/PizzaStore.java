package com.example.halo.demo.设计模式23.abstractfactorymethod;

/**
 * @Description: 披萨商店
 * @Author: Halo_ry
 * @Date: 2020/3/31 19:26
 */
public abstract class PizzaStore {
    public Pizza orderPizza(String type) {
        Pizza pizza;
        pizza = createPizza(type);
        pizza.prepare();
        pizza.bake();
        pizza.cut();
        pizza.box();
        return pizza;
    }

    /*
     * 创建pizza的方法交给子类去实现
     */
    abstract Pizza createPizza(String type);
}
