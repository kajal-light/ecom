package com.ecommersce.productservice.controller;


import com.exception.InvalidProductException;
import com.exception.model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ProductControllerAdvice {

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> NoSuchElementExceptionException(NoSuchElementException ex) {

        return new ResponseEntity<>("NO data is present in Product service DB ,please change your request", HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<ErrorDetails> handleInvalidProductException(InvalidProductException ex) {

        return new ResponseEntity<>(ex.getErrorDetails(), HttpStatus.PRECONDITION_FAILED);
    }

}
