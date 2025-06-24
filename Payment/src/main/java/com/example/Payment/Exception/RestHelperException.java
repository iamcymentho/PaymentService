package com.example.Payment.Exception;

public class RestHelperException extends Exception{
    public RestHelperException(String message) {
        super(message);
    }
    public RestHelperException(String message, Throwable cause) {
        super(message, cause);
    }

}
