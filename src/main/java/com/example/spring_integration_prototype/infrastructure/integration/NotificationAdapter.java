package com.example.spring_integration_prototype.infrastructure.integration;

import com.example.spring_integration_prototype.application.port.out.NotificationPort;
import com.example.spring_integration_prototype.domain.model.OrderPlacedEvent;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.stereotype.Component;

@Component
public class NotificationAdapter implements NotificationPort {

    private final NotificationGateway notificationGateway;

    public NotificationAdapter(NotificationGateway notificationGateway) {
        this.notificationGateway = notificationGateway;
    }

    @Override
    public void notify(OrderPlacedEvent event) {
        notificationGateway.sendNotification(event);
    }

    @MessagingGateway
    public interface NotificationGateway {
        @Gateway(requestChannel = "orderPlacedChannel")
        void sendNotification(OrderPlacedEvent event);
    }
}
