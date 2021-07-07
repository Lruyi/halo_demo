package com.example.halo.demo.observer;

/**
 * @Description: 测试程序
 * @Author: Halo_ry
 * @Date: 2020/3/30 17:20
 */
public class WeatherStationTest {
    public static void main(String[] args) {
        WeatherData weatherData = new WeatherData();
        CurrentConditionsDisplay conditionsDisplay = new CurrentConditionsDisplay(weatherData);
        CurrentConditionsDisplay conditionsDisplay2 = new CurrentConditionsDisplay(weatherData);
        CurrentConditionsDisplay conditionsDisplay3 = new CurrentConditionsDisplay(weatherData);
        weatherData.setMeasurements(80, 65, 30.4f);
        weatherData.setMeasurements(82, 70, 29.2f);
        weatherData.setMeasurements(78, 78, 40.4f);
    }
}
