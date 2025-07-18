package com.example.spring_integration_prototype.application.port.in;

import com.example.spring_integration_prototype.domain.model.Order;

public interface PlaceOrderUseCase {
    Order placeOrder(String product, double price);
}
