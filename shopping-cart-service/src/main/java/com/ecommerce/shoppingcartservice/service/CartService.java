package com.ecommerce.shoppingcartservice.service;

import org.springframework.stereotype.Component;

@Component
public interface CartService {
    void addCartItem(String userId, String productId, int quantity);

    void deleteCartItem(String userId, Long itemId);


    void checkOut(String userId);
}
