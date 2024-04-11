package com.ecommersce.productservice.controller;


import com.exception.InvalidProductException;
import com.exception.NoProductFoundException;
import com.exception.OutOfStockException;
import com.exception.model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ProductControllerAdvice {



    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<ErrorDetails> handleInvalidProductException(InvalidProductException ex) {

        return new ResponseEntity<>(ex.getErrorDetails(), HttpStatus.PRECONDITION_FAILED);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoProductFoundException.class)
    public ResponseEntity<ErrorDetails> noProductFoundExceptionException(NoProductFoundException ex) {

        return new ResponseEntity<>(ex.getErrorDetails(), HttpStatus.PRECONDITION_FAILED);
    }
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<ErrorDetails> outOfStockExceptionException(OutOfStockException ex) {

        return new ResponseEntity<>(ex.getErrorDetails(), HttpStatus.PRECONDITION_FAILED);
    }

}
