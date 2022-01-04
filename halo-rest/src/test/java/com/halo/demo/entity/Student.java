package com.halo.demo.entity;

import java.io.Serializable;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2020/3/31 8:08
 */
public class Student implements Serializable {
    private static final long serialVersionUID = 5273493579596713212L;
    private int age;
    private String name;

    public Student(String name, int age) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student{" + "age=" + age + ", name='" + name + '\'' + '}';
    }
}
