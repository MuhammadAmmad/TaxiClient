package com.example.roman.test.data;

import com.orm.SugarRecord;

public class AirOrder extends SugarRecord {
    private String orderId;
    private String description;
    private String from;
    private boolean isPrevious;

    AirOrder() { }

    public AirOrder(Order order) {
        this.orderId = order.getOrderId();
        this.description = order.getDescription();
        this.from = order.getFrom();
        this.isPrevious = order.getIsPrevious();
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public boolean isPrevious() {
        return isPrevious;
    }

    public void setIsPrevious(boolean previous) {
        isPrevious = previous;
    }
}
