package com.ecommerce.shoppingcartservice.service;

import com.ecommerce.dto.PaymentResponse;
import org.springframework.stereotype.Component;

@Component
public interface CartService {
    String addCartItem(String userId, String productId, int quantity);

    String deleteCartItem(String userId, Long itemId);


    PaymentResponse checkOut(String userId) throws Exception;
}
