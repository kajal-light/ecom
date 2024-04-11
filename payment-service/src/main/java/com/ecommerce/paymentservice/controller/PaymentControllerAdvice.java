package com.ecommerce.paymentservice.controller;

import com.exception.InsufficientBalanceException;
import com.exception.OutOfStockException;
import com.exception.model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
public class PaymentControllerAdvice {

    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorDetails> InsufficientBalanceException(InsufficientBalanceException ex) {

        return new ResponseEntity<>(ex.getErrorDetails(), HttpStatus.PRECONDITION_FAILED);
    }

}
