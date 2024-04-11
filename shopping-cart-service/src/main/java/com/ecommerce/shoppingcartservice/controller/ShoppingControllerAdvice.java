package com.ecommerce.shoppingcartservice.controller;

import com.exception.EmptyInputException;
import com.exception.NoProductFoundException;
import com.exception.model.ErrorDetails;
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoProductFoundException.class)
    public ResponseEntity<ErrorDetails> noProductFoundExceptionException(NoProductFoundException ex) {

        return new ResponseEntity<>(ex.getErrorDetails(), HttpStatus.PRECONDITION_FAILED);
    }
}

