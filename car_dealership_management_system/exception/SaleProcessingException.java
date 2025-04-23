package com.cardealership.managementsystem.exception;

public class SaleProcessingException extends RuntimeException {

    public SaleProcessingException(String message) {
        super(message);
    }

    public SaleProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}