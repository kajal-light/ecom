package com.exception;

import com.exception.model.ErrorDetails;

public class OutOfStockException extends RuntimeException {
    private final ErrorDetails errorDetails;

    public OutOfStockException(ErrorDetails errorDetails) {
        super();
        this.errorDetails = errorDetails;
    }

    public ErrorDetails getErrorDetails() {
        return errorDetails;
    }
}


