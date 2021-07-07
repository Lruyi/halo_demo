package com.example.halo.demo.abstractfactorymethod;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2020/3/31 19:20
 */
public class CheesePizza extends Pizza {

    private PizzaIngredientFactory pizzaIngredientFactory;

    public CheesePizza(PizzaIngredientFactory pizzaIngredientFactory) {
        this.pizzaIngredientFactory = pizzaIngredientFactory;
        prepare();
    }

    /**
     * 方法一步一步地创建芝士比萨，每当需要原料时，就跟工厂要
     */
    @Override
    public void prepare() {
        System.out.println("Prepareing " + name);
        dough = pizzaIngredientFactory.createDough();
        sauce = pizzaIngredientFactory.createSauce();
        cheese = pizzaIngredientFactory.createCheese();
    }
}
