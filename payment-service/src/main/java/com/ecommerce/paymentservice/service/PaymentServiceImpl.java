package com.ecommerce.paymentservice.service;

import com.ecommerce.dto.PaymentRequest;
import com.ecommerce.dto.PaymentResponse;
import com.ecommerce.dto.PaymentStatus;
import com.ecommerce.entity.Payment;
import com.ecommerce.paymentservice.repository.PaymentRepository;
import com.ecommerce.paymentservice.repository.UserRepository;
import com.exception.InsufficientBalanceException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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
            payment.setPaymentId(UUID.randomUUID().toString());
            payment.setPaymentStatus(PaymentStatus.COMPLETED.toString());
            payment = paymentRepository.save(payment);
            BeanUtils.copyProperties(payment, paymentResponse);
            paymentResponse.setPaymentDate(LocalDateTime.now());
            paymentResponse.setPaymentStatus(PaymentStatus.COMPLETED);
            return paymentResponse;
        } else if (balance < 0) {
            BeanUtils.copyProperties(paymentRequest, paymentResponse);
            paymentResponse.setPaymentDate(LocalDateTime.now());
            payment.setPaymentStatus(PaymentStatus.FAILED.toString());
            payment.setPaymentId(UUID.randomUUID().toString());
            paymentRepository.save(payment);
            throw new InsufficientBalanceException("TF_001", "User has less available amount in bank than the order amount");

        } else {
            System.out.println("BALANCE 0 after the order");
            BeanUtils.copyProperties(paymentRequest, payment);
            payment.setPaymentId(UUID.randomUUID().toString());
            payment.setPaymentStatus(PaymentStatus.COMPLETED.toString());
            payment = paymentRepository.save(payment);
            BeanUtils.copyProperties(payment, paymentResponse);
            paymentResponse.setPaymentDate(LocalDateTime.now());
            paymentResponse.setPaymentStatus(PaymentStatus.COMPLETED);
            return paymentResponse;
        }

    }
}
