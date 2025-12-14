package com.library.exception;

/**
 * Custom exception for business logic errors
 */
public class BusinessException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}

