package com.ecommerce.shoppingcartservice.controller;

import com.ecommerce.exception.NoProductFoundException;
import com.ecommerce.exception.dto.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ShoppingCartControllerAdvice {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoProductFoundException.class)
    public ResponseEntity<ErrorDetails> noProductFoundExceptionException(NoProductFoundException ex) {
        return new ResponseEntity<>(ex.getErrorDetails(), HttpStatus.PRECONDITION_FAILED);
    }
}

