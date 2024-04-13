package com.ecommerce.orderservice.service;



import com.ecommerce.dto.EcommerceGenericResponse;
import com.ecommerce.dto.OrderServiceRequestDTO;
import com.ecommerce.dto.OrderServiceResponse;
import com.ecommerce.dto.PaymentResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public interface OrderService {
    ResponseEntity<EcommerceGenericResponse> placeOrder(OrderServiceRequestDTO orderDto) throws JsonProcessingException;
}
