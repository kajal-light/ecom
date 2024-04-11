package com.exception;

import com.exception.model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmptyInputException extends RuntimeException {
    private final ErrorDetails errorDetails;


    public EmptyInputException(ErrorDetails errorDetails) {
        super();
        this.errorDetails = errorDetails;
    }

    public ErrorDetails getErrorDetails() {
        return errorDetails;
    }
}
