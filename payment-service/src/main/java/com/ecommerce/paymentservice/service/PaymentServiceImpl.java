package com.ecommerce.paymentservice.service;

import com.ecommerce.paymentservice.dto.PaymentRequest;
import com.ecommerce.paymentservice.dto.PaymentResponse;
import com.ecommerce.paymentservice.dto.PaymentStatus;
import com.ecommerce.paymentservice.entity.Payment;
import com.ecommerce.paymentservice.exception.InsufficientBalanceException;
import com.ecommerce.paymentservice.repository.PaymentRepository;
import com.ecommerce.paymentservice.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        String userId = paymentRequest.getUserId();
        BigDecimal availableBalance = userRepository.findAvailableBalanceByUserId(userId);
        int balance = availableBalance.compareTo(paymentRequest.getOrderAmount());
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPaymentDate(LocalDateTime.now());
        paymentResponse.setPaymentStatus(PaymentStatus.IN_PROGRESS);
        Payment payment = new Payment();
        if (balance > 0) {
            BeanUtils.copyProperties(paymentRequest, payment);
            payment = paymentRepository.save(payment);
            BeanUtils.copyProperties(payment, paymentResponse);
            paymentResponse.setPaymentDate(LocalDateTime.now());
            paymentResponse.setPaymentStatus(PaymentStatus.COMPLETED);
            return paymentResponse;
        } else if (balance < 0) {
            BeanUtils.copyProperties(paymentRequest, paymentResponse);
            paymentResponse.setPaymentDate(LocalDateTime.now());
            paymentResponse.setPaymentStatus(PaymentStatus.FAILED);
           throw new InsufficientBalanceException("User has less available amount in bank than the order amount");

        } else {
            System.out.println("BALANCE 0 after the order");
            BeanUtils.copyProperties(paymentRequest, payment);
            payment = paymentRepository.save(payment);
            BeanUtils.copyProperties(payment, paymentResponse);
            paymentResponse.setPaymentDate(LocalDateTime.now());
            paymentResponse.setPaymentStatus(PaymentStatus.COMPLETED);
            return paymentResponse;
        }

    }
}
