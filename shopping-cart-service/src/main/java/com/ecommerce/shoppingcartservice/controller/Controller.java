package com.ecommerce.shoppingcartservice.controller;

import com.ecommerce.shoppingcartservice.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shopping-cart")
public class Controller {

   @Autowired
   private CartService cartService;


//addcart
@PostMapping("/addCartItem/userId/{userId}/productId/{productId}/quantity/{quantity}")
public ResponseEntity<String> addCartItem(@PathVariable String userId,@PathVariable String productId,@PathVariable int quantity){

    cartService.addCartItem(userId,productId,quantity);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }


    @DeleteMapping("/deleteCartItem/userId/{userId}/itemId/{itemId}")
    public ResponseEntity<String> deleteCartItem(@PathVariable String userId,@PathVariable Long itemId){

        cartService.deleteCartItem(userId,itemId);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @PostMapping("/placeOrder/userId/{userId}")
    public ResponseEntity<String> checkOut(@PathVariable String userId){

        cartService.checkOut(userId);


        return null;
    }





}
