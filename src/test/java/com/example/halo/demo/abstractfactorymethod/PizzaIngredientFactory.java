package com.example.halo.demo.abstractfactorymethod;

/**
 * @Description: 原料工厂接口
 * @Author: Halo_ry
 * @Date: 2020/3/31 18:53
 */
public interface PizzaIngredientFactory {
    /**
     * 在接口中，每个原料都有一个对应的方法创建该原料
     */
    // 创建面团
    public Dough createDough();

    // 创建酱
    public Sauce createSauce();

    // 创建奶酪
    public Cheese createCheese();

    // 创建蔬菜
    public Veggies[] createVeggies();

    // 创建意大利辣香肠
    public Pepperoni createPepperoni();

}
