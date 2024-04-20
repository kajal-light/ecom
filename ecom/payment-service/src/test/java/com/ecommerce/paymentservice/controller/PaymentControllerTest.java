package com.ecommerce.paymentservice.controller;

import com.ecommerce.dto.*;

import com.ecommerce.paymentservice.service.PaymentService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
@SpringBootTest
public class PaymentControllerTest {

    @InjectMocks
    private PaymentController orderController;
    @Mock
    private PaymentService paymentService;


    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void placeOrder(){
        String userId="shd";
        PaymentResponse response=new PaymentResponse();
        response.setUserId(userId);
        response.setPaymentStatus(PaymentStatus.COMPLETED);
        response.setOrderDate(LocalDateTime.now());
        response.setPaymentMethod(PaymentType.COD);
        response.setOrderAmount(String.valueOf(23.00));
        response.setPaymentDate(LocalDateTime.now());

        PaymentRequest mockPaymentRequest=new PaymentRequest();
        mockPaymentRequest.setPaymentMethod(PaymentType.COD);
        mockPaymentRequest.setOrderAmount(BigDecimal.valueOf(23.00));
        mockPaymentRequest.setUserId(userId);
        mockPaymentRequest.setDateOfOrder(LocalDateTime.now());
        mockPaymentRequest.setPaymentMethod(PaymentType.COD);
        when(paymentService.processPayment(mockPaymentRequest)).thenReturn(response);
        ResponseEntity<PaymentResponse> paymentResponse=orderController.processPayment(mockPaymentRequest);
        assertEquals(mockPaymentRequest.getUserId(), response.getUserId());
    }
}


