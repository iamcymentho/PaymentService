package com.example.Payment.Exception;

import com.example.Payment.ApiResponse.ApiResponse;
import com.example.Payment.Domain.Enum.ErrorCode;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PaymentProcessingException.class)
    public ResponseEntity<ApiResponse<Void>> handlePaymentProcessingException(PaymentProcessingException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        HttpStatus status = mapErrorCodeToHttpStatus(errorCode);
        return ResponseEntity.status(status)
                .body(ApiResponse.error(errorCode.name(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("VALIDATION_ERROR", errorMessage));
    }

    private HttpStatus mapErrorCodeToHttpStatus(ErrorCode errorCode) {
        switch (errorCode) {
            case INVALID_REQUEST:
                return HttpStatus.BAD_REQUEST;
            case SERVICE_UNAVAILABLE:
                return HttpStatus.SERVICE_UNAVAILABLE;
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
