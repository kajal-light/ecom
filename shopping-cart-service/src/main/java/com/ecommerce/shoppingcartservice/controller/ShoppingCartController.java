package com.ecommerce.shoppingcartservice.controller;

import com.ecommerce.dto.PaymentResponse;
import com.ecommerce.shoppingcartservice.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shopping-cart")
public class ShoppingCartController {


   private final CartService cartService;
    @Autowired
    public ShoppingCartController(CartService cartService) {
        this.cartService = cartService;
    }


    //addcart
@PostMapping("/addCartItem/userId/{userId}/productId/{productId}/quantity/{quantity}")
public ResponseEntity<String> addCartItem(@PathVariable String userId,@PathVariable String productId,@PathVariable int quantity){


        return new ResponseEntity<>(cartService.addCartItem(userId,productId,quantity),HttpStatus.CREATED);

    }


    @DeleteMapping("/deleteCartItem/userId/{userId}/itemId/{itemId}")
    public ResponseEntity<String> deleteCartItem(@PathVariable String userId,@PathVariable Long itemId){

       ;
        return new ResponseEntity<>( cartService.deleteCartItem(userId,itemId),HttpStatus.OK);

    }

    @PostMapping("/placeOrder/userId/{userId}")
    public ResponseEntity<PaymentResponse> checkOut(@PathVariable String userId) throws Exception {


         return new ResponseEntity<>(cartService.checkOut(userId), HttpStatus.CREATED);


    }





}
