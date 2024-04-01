package com.ecommerce.orderservice.service;


import com.ecommerce.orderservice.dto.OrderServiceRequestDTO;
import com.ecommerce.orderservice.dto.OrderServiceResponse;
import org.springframework.stereotype.Component;

@Component
public interface OrderService {
    OrderServiceResponse placeOrder(OrderServiceRequestDTO orderDto);
}
