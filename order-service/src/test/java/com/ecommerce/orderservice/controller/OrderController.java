package com.ecommerce.orderservice.controller;

import com.ecommerce.dto.OrderServiceRequestDTO;
import com.ecommerce.dto.PaymentResponse;
import com.ecommerce.dto.ProductDTO;
import com.ecommerce.orderservice.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

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

       ProductDTO productDTO=new ProductDTO();
       productDTO.setProductId("dhdh");
       productDTO.setProductPrice(2344.0);

       List<ProductDTO> listOfProduct=new ArrayList<>();
       listOfProduct.add(productDTO);
       OrderServiceRequestDTO request=new OrderServiceRequestDTO();
       request.setProducts(listOfProduct);
       request.setUserId(UserId);
       request.setTotalAmount(246.00);

       orderController.placeOrder(request);
       Assertions.assertEquals(request.getUserId(),response.getUserId());
   }
}
