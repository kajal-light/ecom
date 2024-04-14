package com.ecommerce.exception;

import com.ecommerce.exception.dto.ErrorDetails;

import java.util.List;

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





