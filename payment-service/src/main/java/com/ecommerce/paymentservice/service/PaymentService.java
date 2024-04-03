package com.ecommerce.paymentservice.service;

import com.ecommerce.paymentservice.dto.PaymentRequest;
import com.ecommerce.paymentservice.dto.PaymentResponse;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest paymentRequest);
}
