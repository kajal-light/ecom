package com.ecommerce.orderservice.service;


import com.ecommerce.dto.EcommerceGenericResponse;
import com.ecommerce.dto.OrderServiceRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public interface OrderService {
    ResponseEntity<EcommerceGenericResponse> placeOrder(OrderServiceRequestDTO orderDto) ;
}
