package com.example.spring_integration_prototype.application.port.out;

import com.example.spring_integration_prototype.domain.model.OrderPlacedEvent;

public interface NotificationPort {
    void notify(OrderPlacedEvent event);
}
