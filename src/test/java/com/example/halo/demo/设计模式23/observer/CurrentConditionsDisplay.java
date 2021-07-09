package com.example.halo.demo.设计模式23.observer;

/**
 * @Description: 布告板
 * @Author: Halo_ry
 * @Date: 2020/3/30 17:15
 */
public class CurrentConditionsDisplay implements Observer, DisplayElement {
    // 温度
    private float temperature;
    // 湿度
    private float humidity;
    // 天气数据
    private Subject weatherData;

    public CurrentConditionsDisplay(Subject weatherData) {
        this.weatherData = weatherData;
        weatherData.registerObserver(this);
    }

    @Override
    public void display() {
        System.out.println("Current conditions:" + temperature + "F degrees and " + humidity + "% humidity");
    }

    @Override
    public void update(float temp, float humidity, float pressure) {
        this.temperature = temp;
        this.humidity = humidity;
        display();
    }
}
