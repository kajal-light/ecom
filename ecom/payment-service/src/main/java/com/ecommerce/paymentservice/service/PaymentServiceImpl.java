package com.ecommerce.paymentservice.service;

import com.ecommerce.dto.PaymentRequest;
import com.ecommerce.dto.PaymentResponse;
import com.ecommerce.dto.PaymentStatus;
import com.ecommerce.dto.PaymentType;
import com.ecommerce.entity.Payment;
import com.ecommerce.paymentservice.constants.PaymentServiceConstants;
import com.ecommerce.paymentservice.repository.PaymentRepository;
import com.ecommerce.paymentservice.repository.UserRepository;
import com.ecommerce.exception.InsufficientBalanceException;
import com.ecommerce.exception.dto.ErrorDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;


    private final UserRepository userRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        PaymentResponse response = new PaymentResponse();

        if (!paymentRequest.getPaymentMethod().equals(PaymentType.COD)) {
            proceedPaymentForCreditCard(paymentRequest, response);

        } else {
            Payment payment = new Payment();

            payment.setPaymentMethod(PaymentType.COD);
            payment.setOrderAmount(paymentRequest.getOrderAmount());
            payment.setPaymentId(UUID.randomUUID().toString());
            payment.setPaymentStatus(String.valueOf(PaymentStatus.PENDING));
            payment.setUserId(paymentRequest.getUserId());
            payment.setOrderDate(LocalDateTime.now());
            paymentRepository.save(payment);


            response.setPaymentStatus(PaymentStatus.PENDING);
            response.setPaymentMethod(PaymentType.COD);
            response.setOrderAmount(String.valueOf(paymentRequest.getOrderAmount()));
            response.setUserId(paymentRequest.getUserId());
            response.setOrderDate(paymentRequest.getDateOfOrder());

        }

        return response;
    }

    private void proceedPaymentForCreditCard(PaymentRequest paymentRequest, PaymentResponse paymentResponse) {

        String userId = paymentRequest.getUserId();
        BigDecimal availableBalance;
        if (!paymentRequest.getPaymentMethod().equals(PaymentType.CREDIT_CARD)) {
            availableBalance = userRepository.findAvailableBalanceByUserId(userId);
        } else {
            availableBalance = userRepository.findAvailableCreditLimitByUserId(userId);
        }
        int balance = availableBalance.compareTo(paymentRequest.getOrderAmount());

        paymentResponse.setPaymentDate(LocalDateTime.now());
        paymentResponse.setPaymentStatus(PaymentStatus.IN_PROGRESS);
        Payment payment = new Payment();
        if (balance > 0) {

            BeanUtils.copyProperties(paymentRequest, payment);
            payment.setPaymentId(UUID.randomUUID().toString());
            payment.setPaymentStatus(PaymentStatus.COMPLETED.toString());

            payment.setPaymentDate(LocalDateTime.now());
            payment.setOrderDate(paymentRequest.getDateOfOrder());
            payment = paymentRepository.save(payment);
            BeanUtils.copyProperties(payment, paymentResponse);
            paymentResponse.setPaymentDate(LocalDateTime.now());
            paymentResponse.setPaymentStatus(PaymentStatus.COMPLETED);
            paymentResponse.setOrderAmount(String.valueOf(paymentRequest.getOrderAmount()));
            paymentResponse.setOrderDate(paymentRequest.getDateOfOrder());

        } else if (balance < 0) {

            BeanUtils.copyProperties(paymentRequest, paymentResponse);
            paymentResponse.setPaymentDate(LocalDateTime.now());
            payment.setPaymentStatus(PaymentStatus.FAILED.toString());
            payment.setPaymentId(UUID.randomUUID().toString());
            payment.setPaymentDate(LocalDateTime.now());
            payment.setOrderDate(paymentRequest.getDateOfOrder());
            paymentRepository.save(payment);
            throw new InsufficientBalanceException(new ErrorDetails(HttpStatus.EXPECTATION_FAILED, PaymentServiceConstants.INSUFFICIENT_BALANCE, PaymentServiceConstants.INSUFFICIENT_BALANCE_CODE, PaymentServiceConstants.SERVICE_NAME, LocalDateTime.now().toString()));

        } else {
            log.info("BALANCE 0 after the order");

            BeanUtils.copyProperties(paymentRequest, payment);
            payment.setPaymentId(UUID.randomUUID().toString());
            payment.setPaymentStatus(PaymentStatus.COMPLETED.toString());
            payment.setPaymentDate(LocalDateTime.now());
            payment.setOrderDate(paymentRequest.getDateOfOrder());
            payment = paymentRepository.save(payment);
            BeanUtils.copyProperties(payment, paymentResponse);
            paymentResponse.setPaymentDate(LocalDateTime.now());
            paymentResponse.setPaymentStatus(PaymentStatus.COMPLETED);
            paymentResponse.setOrderAmount(String.valueOf(paymentRequest.getOrderAmount()));
            paymentResponse.setOrderDate(paymentRequest.getDateOfOrder());

        }

    }


}
