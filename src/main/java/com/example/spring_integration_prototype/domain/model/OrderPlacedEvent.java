package com.example.spring_integration_prototype.domain.model;

import java.util.UUID;

public class OrderPlacedEvent {
    private final UUID orderId;
    private final String product;

    public OrderPlacedEvent(UUID orderId, String product) {
        this.orderId = orderId;
        this.product = product;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public String getProduct() {
        return product;
    }
}
