package com.ecommerce.orderservice.controller;

import com.ecommerce.dto.OrderServiceRequestDTO;
import com.ecommerce.dto.PaymentResponse;
import com.ecommerce.dto.OrderedProductDTO;
import com.ecommerce.orderservice.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
 class OrderControllerTest{
    @InjectMocks
    private OrderController orderController;
   @Mock
    private  OrderService orderService;



   @Test
    void placeOrder(){
       String UserId="shd";
       PaymentResponse response=new PaymentResponse();
       response.setPaymentDate(LocalDateTime.now());
       response.setOrderAmount(String.valueOf(246.00));
       response.setUserId(UserId);

       OrderedProductDTO orderedProductDTO =new OrderedProductDTO();
       orderedProductDTO.setProductId("dhdh");
       orderedProductDTO.setProductPrice(2344.0);

       List<OrderedProductDTO> listOfProduct=new ArrayList<>();
       listOfProduct.add(orderedProductDTO);
       OrderServiceRequestDTO request=new OrderServiceRequestDTO();
       request.setProducts(listOfProduct);
       request.setUserId(UserId);
       request.setTotalAmount(246.00);

       orderController.placeOrder(request);
       Assertions.assertEquals(request.getUserId(),response.getUserId());
   }
}
