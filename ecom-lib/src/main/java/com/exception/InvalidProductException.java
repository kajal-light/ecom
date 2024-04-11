package com.exception;

import com.exception.model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class InvalidProductException extends RuntimeException {
    private final ErrorDetails errorDetails;

    public InvalidProductException(ErrorDetails errorDetails) {
        super();
        this.errorDetails = errorDetails;
    }

    public ErrorDetails getErrorDetails() {
        return errorDetails;
    }
}

