package com.ecommerce.orderservice.service;


import com.ecommerce.orderservice.model.OrderServiceRequestDTO;
import com.ecommerce.orderservice.model.OrderServiceResponse;
import org.springframework.stereotype.Component;

@Component
public interface OrderService {
    OrderServiceResponse placeOrder(OrderServiceRequestDTO orderDto);
}
