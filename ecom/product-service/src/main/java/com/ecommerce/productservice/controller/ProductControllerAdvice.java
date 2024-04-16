package com.ecommerce.productservice.controller;


import com.ecommerce.exception.InvalidProductException;
import com.ecommerce.exception.NoProductFoundException;
import com.ecommerce.exception.OutOfStockException;
import com.ecommerce.exception.dto.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProductControllerAdvice {



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoProductFoundException.class)
    public ResponseEntity<ErrorDetails> handleProductFoundExceptionException(NoProductFoundException ex) {

        return new ResponseEntity<>(ex.getErrorDetails(), HttpStatus.PRECONDITION_FAILED);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<ErrorDetails> outOfStockExceptionException(OutOfStockException ex) {

        return new ResponseEntity<>(ex.getErrorDetails(),  HttpStatus.PRECONDITION_FAILED);
    }

}
