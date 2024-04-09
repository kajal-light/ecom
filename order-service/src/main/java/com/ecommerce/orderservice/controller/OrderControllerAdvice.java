package com.ecommerce.orderservice.controller;

import com.exception.InsufficientBalanceException;
import com.exception.model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class OrderControllerAdvice {

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> NoSuchElementExceptionException(NoSuchElementException ex) {

        return new ResponseEntity<String>(ex.getMessage(),HttpStatus.NOT_FOUND);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> NoSuchElementExceptionException(RuntimeException ex) {

        return new ResponseEntity<String>(ex.getMessage(),HttpStatus.NOT_FOUND);
    }
}
