package com.example.Payment.Domain;

import com.example.Payment.Domain.Enum.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
public class PaymentResponse {
    private String paymentReference;
    private String authorizationUrl;
    private PaymentStatus status;
    private String providerMessage;
    private Map<String, String> metadata;
    private Instant createdAt;
    private Instant expiresAt;
}
