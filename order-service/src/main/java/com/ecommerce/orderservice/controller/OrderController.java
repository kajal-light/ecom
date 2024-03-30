package com.ecommerce.orderservice.controller;


import com.ecommerce.orderservice.model.OrderServiceRequestDTO;
import com.ecommerce.orderservice.model.OrderServiceResponse;
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
    @Autowired
    private OrderService orderService;


    @PostMapping("/placeOrder")
    public ResponseEntity<OrderServiceResponse> placeOrder(@RequestBody OrderServiceRequestDTO orderDto) {

        return ResponseEntity.ok(orderService.placeOrder(orderDto));

    }


}
