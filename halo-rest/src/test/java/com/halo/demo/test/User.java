package com.halo.demo.test;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2020/5/10 10:11
 */
public class User {
    //用户id
    private int id;
    //用户姓名
    private String name;
    //用户年级
    private int grade;
    //年龄
    private int age;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    //按照年级和年龄分组。要求相同年级和相同年龄的人，放在一起
    private static List<List<User>> partUser(List<User> users) {
        Map<String, List<User>> collect = users.stream().collect(Collectors.groupingBy((e -> e.getGrade() + "_" + e.getAge())
                , mapping(Function.identity(), toList())));

//        Map<String, List<User>> collect1 = users.stream().collect(Collectors.groupingBy(e -> e.getGrade() + "_" + e.getAge()));

        return null;
    }



}
