package com.example.halo.demo.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: WeatherData实现主题接口
 * @Author: Halo_ry
 * @Date: 2020/3/30 17:03
 */
public class WeatherData implements Subject {
    private List<Observer> observers;
    private float tempterature;
    private float pressure;
    private float humidity;

    public WeatherData() {
        this.observers = new ArrayList<>();
    }

    @Override
    public void notifyObserver() {
        for (int i = 0; i < observers.size(); i++) {
            Observer observer = observers.get(i);
            observer.update(tempterature, humidity, pressure);
        }
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeOberver(Observer observer) {
        int i = observers.indexOf(observer);
        if (i >= 0) {
            observers.remove(i);
        }
    }

    /**
     * 气象站得到更新的观测数据时，通知观察者
     */
     public void measurementChanged(){
         notifyObserver();
     }

    public void setMeasurements(float temperature,float humidity,float pressure){
         this.tempterature = temperature;
         this.humidity = humidity;
         this.pressure = pressure;
         measurementChanged();
     }
}
