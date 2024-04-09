package com.ecommersce.productservice.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ProductControllerAdvice {

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> NoSuchElementExceptionException(NoSuchElementException ex) {

        return new ResponseEntity<String>("NO data is present in Product service DB ,please change your request",HttpStatus.NOT_FOUND);
    }

}
