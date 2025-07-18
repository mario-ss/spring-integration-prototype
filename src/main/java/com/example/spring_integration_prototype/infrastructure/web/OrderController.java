package com.example.spring_integration_prototype.infrastructure.web;

import com.example.spring_integration_prototype.application.port.in.PlaceOrderUseCase;
import com.example.spring_integration_prototype.domain.model.Order;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final PlaceOrderUseCase placeOrderUseCase;

    public OrderController(PlaceOrderUseCase placeOrderUseCase) {
        this.placeOrderUseCase = placeOrderUseCase;
    }

    @PostMapping("/orders")
    public Order placeOrder(@RequestBody OrderRequest request) {
        return placeOrderUseCase.placeOrder(request.getProduct(), request.getPrice());
    }

    public static class OrderRequest {
        private String product;
        private double price;

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }
}
