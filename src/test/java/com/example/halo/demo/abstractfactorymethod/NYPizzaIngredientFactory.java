package com.example.halo.demo.abstractfactorymethod;

/**
 * @Description: 纽约原料工厂
 * @Author: Halo_ry
 * @Date: 2020/3/31 19:02
 */
public class NYPizzaIngredientFactory implements PizzaIngredientFactory {
    @Override
    public Dough createDough() {
        return new Dough();
    }

    @Override
    public Sauce createSauce() {
        return new Sauce();
    }

    @Override
    public Cheese createCheese() {
        return new Cheese();
    }

    @Override
    public Veggies[] createVeggies() {
        return new Veggies[0];
    }

    @Override
    public Pepperoni createPepperoni() {
        return new Pepperoni();
    }
}
