package com.example.spring_integration_prototype.application;

import com.example.spring_integration_prototype.application.port.in.PlaceOrderUseCase;
import com.example.spring_integration_prototype.application.port.out.NotificationPort;
import com.example.spring_integration_prototype.domain.model.Order;
import com.example.spring_integration_prototype.domain.model.OrderPlacedEvent;
import org.springframework.stereotype.Service;

@Service
public class OrderService implements PlaceOrderUseCase {

    private final NotificationPort notificationPort;

    public OrderService(NotificationPort notificationPort) {
        this.notificationPort = notificationPort;
    }

    @Override
    public Order placeOrder(String product, double price) {
        Order order = new Order(product, price);
        // In a real application, you would save the order to a database here.
        System.out.println("Order placed: " + order.getId());

        notificationPort.notify(new OrderPlacedEvent(order.getId(), order.getProduct()));

        return order;
    }
}
