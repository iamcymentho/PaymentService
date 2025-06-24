package com.example.Payment.Exception;

import com.example.Payment.Domain.Enum.ErrorCode;

public class PaymentProcessingException extends RuntimeException {
    private final ErrorCode errorCode;

    public PaymentProcessingException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}


