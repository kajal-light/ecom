package com.ecommersce.productservice.controller;

import org.exception.NoDataFoundException;
import org.exception.model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;

@ControllerAdvice
public class ProductControllerAdvice {

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<ErrorDetails> handleNoDataFoundException(NoDataFoundException ex) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST, "NF_001", Arrays.asList(ex.getMessage()));
        return ResponseEntity.badRequest().body(errorDetails);
    }


}
