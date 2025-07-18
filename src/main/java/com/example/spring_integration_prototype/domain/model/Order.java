package com.example.spring_integration_prototype.domain.model;

import java.util.UUID;

public class Order {
    private final UUID id;
    private final String product;
    private final double price;

    public Order(String product, double price) {
        this.id = UUID.randomUUID();
        this.product = product;
        this.price = price;
    }

    public UUID getId() {
        return id;
    }

    public String getProduct() {
        return product;
    }

    public double getPrice() {
        return price;
    }
}
