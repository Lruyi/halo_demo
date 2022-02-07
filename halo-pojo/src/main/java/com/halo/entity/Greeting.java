package com.halo.entity;

import java.io.Serializable;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2020/3/13 22:48
 */
public class Greeting implements Serializable, Cloneable {
    private static final long serialVersionUID = -8246725784199592342L;

    private long id;

    private String content;

    public Greeting() {
    }

    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }

    @Override
    public Greeting clone() {
        Greeting greeting = null;
        try {
            // 浅拷贝
            greeting = (Greeting) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return greeting;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
