package com.ecommerce.shoppingcartservice.controller;

import com.ecommerce.dto.EcommerceGenericResponse;
import com.ecommerce.dto.PaymentResponse;
import com.ecommerce.dto.PaymentStatus;
import com.ecommerce.dto.PaymentType;
import com.ecommerce.shoppingcartservice.service.ShoppingCartService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

@SpringBootTest
 class ShoppingCartControllerTest {

    @InjectMocks
    private ShoppingCartController shoppingCartController;
    @Mock
    private ShoppingCartService shoppingCartService;

    @Test
    void addCartItem(){

        ResponseEntity<EcommerceGenericResponse> response = shoppingCartController.addCartItem("1234","jsdh",3);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }

    @Test
    void deleteCartItem(){

        ResponseEntity<String> response = shoppingCartController.deleteCartItem("1234",2345L);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void checkOut() throws Exception {

        PaymentResponse response=new PaymentResponse();
        response.setUserId("1234");
        response.setPaymentMethod(PaymentType.COD);
        response.setPaymentStatus(PaymentStatus.COMPLETED);
        response.setOrderAmount(String.valueOf(127347));
        response.setPaymentDate(LocalDateTime.now());
        ResponseEntity<EcommerceGenericResponse> ecommerceGenericResponse = new ResponseEntity<EcommerceGenericResponse>(response,HttpStatus.CREATED);
        when( shoppingCartService.checkOut("1234")).thenReturn(ecommerceGenericResponse);
         response = (PaymentResponse) shoppingCartController.checkOut("1234").getBody();
        Assertions.assertNotNull(response);

    }


}
