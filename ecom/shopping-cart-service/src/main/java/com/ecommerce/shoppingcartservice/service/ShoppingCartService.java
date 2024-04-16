package com.ecommerce.shoppingcartservice.service;

import com.ecommerce.dto.EcommerceGenericResponse;
import com.ecommerce.dto.PaymentResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public interface ShoppingCartService {
    ResponseEntity<EcommerceGenericResponse> addCartItem(String userId, String productId, int quantity);

    String deleteCartItem(String userId, Long itemId);

    ResponseEntity<EcommerceGenericResponse>  checkOut(String userId) throws Exception;
}
