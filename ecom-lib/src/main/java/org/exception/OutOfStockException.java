package org.exception;

public class OutOfStockException extends RuntimeException {
    private final String errorCode;


    public OutOfStockException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}


