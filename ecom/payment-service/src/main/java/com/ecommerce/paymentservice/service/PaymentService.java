package com.ecommerce.paymentservice.service;

import com.ecommerce.dto.PaymentRequest;
import com.ecommerce.dto.PaymentResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest paymentRequest);
}
