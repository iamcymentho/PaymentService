package com.example.Payment.Domain.Responses;

import lombok.Data;

@Data
public class InitializePaymentResponse {
    private boolean status;
    private String message;
    private PaymentData data;

    @Data
    public static class PaymentData {
        private String authorization_url;
        private String access_code;
        private String reference;
    }
}
