package com.ecommerce.paymentservice.controller;

import com.ecommerce.exception.InsufficientBalanceException;
import com.ecommerce.exception.dto.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PaymentControllerAdvice {

    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorDetails> handleInsufficientBalanceException(InsufficientBalanceException ex) {

        return new ResponseEntity<>(ex.getErrorDetails(), HttpStatus.PRECONDITION_FAILED);
    }

}
