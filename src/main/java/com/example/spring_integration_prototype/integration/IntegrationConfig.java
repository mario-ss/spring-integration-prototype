package com.example.spring_integration_prototype.integration;

import com.example.spring_integration_prototype.domain.model.OrderPlacedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;

@Configuration
public class IntegrationConfig {

    @Bean
    public MessageChannel orderPlacedChannel() {
        return MessageChannels.publishSubscribe().getObject();
    }

    @Bean
    public IntegrationFlow emailNotificationFlow() {
        return IntegrationFlow.from("orderPlacedChannel")
                .handle(message -> {
                    OrderPlacedEvent event = (OrderPlacedEvent) message.getPayload();
                    System.out.println("Sending email for order: " + event.getOrderId());
                }).get();
    }

    @Bean
    public IntegrationFlow shippingNotificationFlow() {
        return IntegrationFlow.from("orderPlacedChannel")
                .handle(message -> {
                    OrderPlacedEvent event = (OrderPlacedEvent) message.getPayload();
                    System.out.println("Notifying shipping for order: " + event.getOrderId());
                }).get();
    }
}
