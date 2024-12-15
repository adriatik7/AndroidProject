package com.example.androidproject.model;

public class Item {
    private int id;
    private String name;
    private double price;
    private int userId;
    private String category;

    // Constructor
    public Item(int id, String name, double price, int userId, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.userId = userId;
        this.category = category;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getUserId() {
        return userId;
    }

    public String getCategory() {
        return category;
    }

    // Setters (if needed)
    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
