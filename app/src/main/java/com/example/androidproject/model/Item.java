package com.example.androidproject.model;

public class Item {
    private String name;
    private double price;
    private int userId;

    public Item(String name, double price, int userId) {
        this.name = name;
        this.price = price;
        this.userId = userId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for price
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
