package com.ecommerce.shoppingcartservice.controller;

import com.exception.EmptyInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ShoppingControllerAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> NoSuchElementExceptionException(NoSuchElementException ex) {

        return new ResponseEntity<String>(ex.getMessage(),HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(EmptyInputException.class)
    public ResponseEntity<String> EmptyInputExceptionException(EmptyInputException ex) {

        return new ResponseEntity<String>("Input data is invalid,please check request",HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> NoSuchElementExceptionException(RuntimeException ex) {

        return new ResponseEntity<String>(ex.getMessage(),HttpStatus.NOT_FOUND);
    }
}

