package com.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmptyInputException extends RuntimeException{
    private final String errorCode;
    private final String message;


    public EmptyInputException(String errorCode, String message) {
        super();
        this.message = message;
        this.errorCode = errorCode;
    }

}
