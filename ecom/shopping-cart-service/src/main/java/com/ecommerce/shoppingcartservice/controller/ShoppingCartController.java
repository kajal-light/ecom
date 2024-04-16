package com.ecommerce.shoppingcartservice.controller;

import com.ecommerce.dto.EcommerceGenericResponse;
import com.ecommerce.shoppingcartservice.service.ShoppingCartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shopping-cart")
public class ShoppingCartController {


    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }


    //addcart
    @PostMapping("/addCartItem/userId/{userId}/productId/{productId}/quantity/{quantity}")
    public ResponseEntity<EcommerceGenericResponse> addCartItem(@PathVariable String userId, @PathVariable String productId, @PathVariable int quantity) {
        return shoppingCartService.addCartItem(userId, productId, quantity);

    }


    @DeleteMapping("/deleteCartItem/userId/{userId}/itemId/{itemId}")
    public ResponseEntity<String> deleteCartItem(@PathVariable String userId, @PathVariable Long itemId) {
        return new ResponseEntity<>(shoppingCartService.deleteCartItem(userId, itemId), HttpStatus.OK);

    }

    @PostMapping("/placeOrder/userId/{userId}")
    public ResponseEntity<EcommerceGenericResponse> checkOut(@PathVariable String userId) throws Exception {
        return shoppingCartService.checkOut(userId);


    }


}
