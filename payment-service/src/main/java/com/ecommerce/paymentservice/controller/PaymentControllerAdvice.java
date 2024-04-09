package com.ecommerce.paymentservice.controller;

import com.exception.InsufficientBalanceException;
import com.exception.model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
public class PaymentControllerAdvice {

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<ErrorDetails> handleInsufficientBalanceException(InsufficientBalanceException ex) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.PRECONDITION_FAILED, ex.getErrorCode(), Arrays.asList(ex.getMessage()));
        return ResponseEntity.badRequest().body(errorDetails);
    }
}
