package com.ecommerce.orderservice.service;



import com.ecommerce.dto.OrderServiceRequestDTO;
import com.ecommerce.dto.OrderServiceResponse;
import org.springframework.stereotype.Component;

@Component
public interface OrderService {
    OrderServiceResponse placeOrder(OrderServiceRequestDTO orderDto);
}
