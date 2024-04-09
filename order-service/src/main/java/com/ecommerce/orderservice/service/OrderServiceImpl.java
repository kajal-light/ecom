package com.ecommerce.orderservice.service;

import com.ecommerce.dto.*;
import com.ecommerce.orderservice.dao.OrderRepository;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Value("${product.service.url}")
    private String productService;
    @Value("${payment.service.url}")
    private String paymentService;


    RestTemplate restTemplate = new RestTemplate();
    ObjectMapper mapper = new ObjectMapper();
    @Override
    @CircuitBreaker(name = "processPayment", fallbackMethod = "processPaymentFallback")
    @Transactional
    public PaymentResponse placeOrder(OrderServiceRequestDTO orderServiceRequestDTO) {

        PaymentResponse paymentResponse=new PaymentResponse();

        List<ProductDTO> products = orderServiceRequestDTO.getProducts();
        List<String> productIds = products.stream().map(ProductDTO::getProductId).collect(Collectors.toList());

        //making call to product service to fetch stocks for their respective products Id
       try {
           JsonNode stocks = restTemplate.postForObject(productService, productIds, JsonNode.class);
           List<ProductData> stockList = mapper.convertValue(stocks, new TypeReference<List<ProductData>>(){});

           List<ProductData> productsInventory = new ArrayList<>(stockList);

           boolean productOutOfStock=  products.stream()
                   .noneMatch(productInventory -> {
                       String productId = productInventory.getProductId();
                       int orderQuantity = productInventory.getProductQuantity();
                       int stock = productsInventory.stream()
                               .filter(stockItem -> stockItem.getProductId().equals(productId))
                               .mapToInt(ProductData::getStock)
                               .findFirst()
                               .orElse(0);
                       return stock == 0 || stock - orderQuantity < 0;
                   });
           if (productOutOfStock) {
               PaymentRequest paymentRequest=new PaymentRequest();
               paymentRequest.setPaymentMethod(PaymentType.COD);
               paymentRequest.setOrderAmount(BigDecimal.valueOf(orderServiceRequestDTO.getTotalAmount()));
               paymentRequest.setDateOfOrder(LocalDate.now().atStartOfDay());
               paymentRequest.setUserId(orderServiceRequestDTO.getUserId());

               try {
                   paymentResponse = restTemplate.postForObject(paymentService, paymentRequest, PaymentResponse.class);
               }catch (Exception e){
                   throw new RuntimeException(e.getMessage());

               }

           }
       }catch (Exception e){

           throw new RuntimeException(e.getMessage());
       }



        return paymentResponse;
    }



    public String processPaymentFallback(Exception e) {


        return "SERVICE IS DOWN, PLEASE TRY AFTER SOMETIME !!!";
    }



}
