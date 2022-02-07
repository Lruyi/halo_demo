package com.halo.demo.设计模式23.abstractfactorymethod;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2020/3/31 19:16
 */
public abstract class Pizza {
    public String name;
    public Dough dough;
    public Sauce sauce;
    public Cheese cheese;
    public Veggies veggies[];
    public Pepperoni pepperoni;

    public abstract void prepare();

    public void bake() {
        System.out.println("Bake for 25 munites at 350");
    }

    public void cut() {
        System.out.println("Cutting the pizza into diagonal slices");
    }

    public void box() {
        System.out.println("Place pizza in official PizzaStore box");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
