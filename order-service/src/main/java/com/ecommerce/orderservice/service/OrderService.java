package com.ecommerce.orderservice.service;



import com.ecommerce.dto.OrderServiceRequestDTO;
import com.ecommerce.dto.OrderServiceResponse;
import com.ecommerce.dto.PaymentResponse;
import org.springframework.stereotype.Component;

@Component
public interface OrderService {
    PaymentResponse placeOrder(OrderServiceRequestDTO orderDto);
}
