package com.example.bsproperty.bean;

import java.io.Serializable;

public class ProductBean implements Serializable {

    private Long id;
    private String name;
    private double price;
    private String info;
    private Long sid;
    private String feel;
    private String type;
    private int addSum;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public Long getSid() {
        return sid;
    }
    public void setSid(Long sid) {
        this.sid = sid;
    }
    public String getFeel() {
        return feel;
    }
    public void setFeel(String feel) {
        this.feel = feel;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public int getAddSum() {
        return addSum;
    }

    public void setAddSum(int addSum) {
        this.addSum = addSum;
    }
}
