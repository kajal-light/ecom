package com.ecommerce.orderservice.controller;


import com.ecommerce.dto.OrderServiceRequestDTO;

import com.ecommerce.dto.PaymentResponse;
import com.ecommerce.orderservice.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order-service")
public class OrderController {

    private final OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/placeOrder")
    public ResponseEntity<PaymentResponse> placeOrder(@RequestBody OrderServiceRequestDTO orderDto) {


            return ResponseEntity.ok(orderService.placeOrder(orderDto));


    }


}
