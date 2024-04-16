package com.ecommerce.paymentservice.service;

import com.ecommerce.dto.PaymentRequest;
import com.ecommerce.dto.PaymentResponse;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest paymentRequest);
}
