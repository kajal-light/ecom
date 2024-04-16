package com.ecommerce.exception;

import com.ecommerce.exception.dto.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class InsufficientBalanceException extends RuntimeException {
    private final ErrorDetails errorDetails;

    public InsufficientBalanceException(ErrorDetails errorDetails) {
        super();
        this.errorDetails = errorDetails;
    }

    public ErrorDetails getErrorDetails() {
        return errorDetails;
    }
}
