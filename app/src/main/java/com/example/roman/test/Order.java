package com.example.roman.test;

public class Order {
    public final int id;
    public final String from;
    public final String to;
    public final int price;

    public Order(String from, String to, int price, int id) {
        this.from = from;
        this.to = to;
        this.price = price;
        this.id = id;
    }
}
