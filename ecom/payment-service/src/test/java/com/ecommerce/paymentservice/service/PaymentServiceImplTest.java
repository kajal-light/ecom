package com.ecommerce.paymentservice.service;


import com.ecommerce.dto.*;
import com.ecommerce.entity.Payment;

import com.ecommerce.exception.InsufficientBalanceException;

import com.ecommerce.paymentservice.repository.PaymentRepository;
import com.ecommerce.paymentservice.repository.UserRepository;


import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@SpringBootTest
public class PaymentServiceImplTest {


    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private UserRepository userRepository;


    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
     void testPlaceOrderForCod() throws Exception {
        String userId = "shdhdh";
        String paymentId = "kshd";
        Payment payment = getPayment();
        payment.setPaymentId(paymentId);
        payment.setPaymentMethod(PaymentType.COD);
        payment.setUserId(userId);
        payment.setPaymentStatus(String.valueOf(PaymentStatus.PENDING));
        payment.setOrderAmount(BigDecimal.valueOf(456));

        PaymentRequest mockPaymentRequest = getPaymentRequest();
        PaymentResponse response = getPaymentResponse();
        mockPaymentRequest.setPaymentMethod(PaymentType.COD);
        response.setPaymentMethod(PaymentType.COD);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        paymentService.processPayment(mockPaymentRequest);
        assertNotNull(response);
    }

    @Test
    void testPlaceOrderForCreditCard() throws Exception {

        String userId = "shdhdh";
        String paymentId = "kshd";
        Payment payment = getPayment();
        payment.setPaymentId(paymentId);
        payment.setPaymentMethod(PaymentType.CREDIT_CARD);
        payment.setUserId(userId);
        payment.setPaymentStatus(String.valueOf(PaymentStatus.COMPLETED));
        payment.setOrderAmount(BigDecimal.valueOf(456));

        PaymentRequest mockPaymentRequest = getPaymentRequest();
        mockPaymentRequest.setUserId(userId);
        mockPaymentRequest.setPaymentMethod(PaymentType.CREDIT_CARD);
        mockPaymentRequest.setOrderAmount(BigDecimal.valueOf(456));
        mockPaymentRequest.setDateOfOrder(LocalDateTime.now());


        PaymentResponse response = getPaymentResponse();
        mockPaymentRequest.setPaymentMethod(PaymentType.CREDIT_CARD);

        response.setPaymentStatus(PaymentStatus.COMPLETED);
        response.setUserId(userId);
        when(userRepository.findAvailableCreditLimitByUserId(mockPaymentRequest.getUserId())).thenReturn(BigDecimal.valueOf(4500));

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        paymentService.processPayment(mockPaymentRequest);
        assertNotNull(response);
    }

    @Test
    void testForBalanceZeroAfterPayment() throws Exception {

        String userId = "shdhdh";
        String paymentId = "kshd";
        Payment payment = getPayment();
        payment.setPaymentId(paymentId);
        payment.setPaymentMethod(PaymentType.CREDIT_CARD);
        payment.setUserId(userId);
        payment.setPaymentStatus(String.valueOf(PaymentStatus.COMPLETED));
        payment.setOrderAmount(BigDecimal.valueOf(45));

        PaymentRequest mockPaymentRequest = getPaymentRequest();
        mockPaymentRequest.setUserId(userId);
        mockPaymentRequest.setPaymentMethod(PaymentType.CREDIT_CARD);
        mockPaymentRequest.setOrderAmount(BigDecimal.valueOf(45));
        mockPaymentRequest.setDateOfOrder(LocalDateTime.now());


        PaymentResponse response = getPaymentResponse();
        mockPaymentRequest.setPaymentMethod(PaymentType.CREDIT_CARD);

        response.setPaymentStatus(PaymentStatus.COMPLETED);
        response.setUserId(userId);
        when(userRepository.findAvailableCreditLimitByUserId(mockPaymentRequest.getUserId())).thenReturn(BigDecimal.valueOf(45));

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        paymentService.processPayment(mockPaymentRequest);
        assertNotNull(response);
    }

    @Test
    void testForInsufficientBalance() throws Exception {

        String userId = "shdhdh";
        String paymentId = "kshd";
        Payment payment = getPayment();
        payment.setPaymentId(paymentId);
        payment.setPaymentMethod(PaymentType.CREDIT_CARD);
        payment.setUserId(userId);
        payment.setPaymentStatus(String.valueOf(PaymentStatus.FAILED));
        payment.setOrderAmount(BigDecimal.valueOf(456));

        PaymentRequest mockPaymentRequest = getPaymentRequest();
        mockPaymentRequest.setUserId(userId);
        mockPaymentRequest.setPaymentMethod(PaymentType.CREDIT_CARD);
        mockPaymentRequest.setOrderAmount(BigDecimal.valueOf(456));
        mockPaymentRequest.setDateOfOrder(LocalDateTime.now());


        PaymentResponse response = getPaymentResponse();
        mockPaymentRequest.setPaymentMethod(PaymentType.CREDIT_CARD);

        response.setPaymentStatus(PaymentStatus.COMPLETED);
        response.setUserId(userId);
        when(userRepository.findAvailableCreditLimitByUserId(mockPaymentRequest.getUserId())).thenReturn(BigDecimal.valueOf(45));

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        assertThrows(InsufficientBalanceException.class, () -> paymentService.processPayment(mockPaymentRequest));
        assertNotNull(response);

    }

    private Payment getPayment() {
        Payment payment = new Payment();
        payment.setPaymentStatus(String.valueOf(PaymentStatus.FAILED));
        payment.setPaymentDate(LocalDateTime.now());
        payment.setOrderDate(LocalDateTime.now());
        return payment;
    }

    private PaymentResponse getPaymentResponse() {
        PaymentResponse response = new PaymentResponse();
        response.setPaymentStatus(PaymentStatus.PENDING);
        response.setPaymentDate(LocalDateTime.now());

        response.setOrderAmount(String.valueOf(345.00));

        response.setOrderDate(LocalDateTime.now());
        return response;
    }

    private PaymentRequest getPaymentRequest() {
        PaymentRequest request = new PaymentRequest();
        request.setOrderAmount(BigDecimal.valueOf(456.00));
        request.setUserId("hsgd");
        request.setDateOfOrder(LocalDateTime.now());

        return request;

    }


}
