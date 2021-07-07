package com.example.halo.demo.observer;

/**
 * @Description: 观察者接口
 * @Author: Halo_ry
 * @Date: 2020/3/30 16:59
 */
public interface Observer {
    public void update(float temp, float humidity, float pressure);
}
