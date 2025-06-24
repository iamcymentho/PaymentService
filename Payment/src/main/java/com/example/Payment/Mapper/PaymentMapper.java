package com.example.Payment.Mapper;

import com.example.Payment.Domain.Enum.PaymentStatus;
import com.example.Payment.Domain.PaymentRecord;
import com.example.Payment.Domain.PaymentRequest;
import com.example.Payment.Domain.PaymentResponse;
import com.example.Payment.Domain.Requests.InitializePaymentRequest;
import com.example.Payment.Domain.Responses.InitializePaymentResponse;
import com.example.Payment.Exception.PaymentMappingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PaymentMapper {
    private final ObjectMapper objectMapper;

    public InitializePaymentRequest toExternalRequest(PaymentRequest request) {
        return InitializePaymentRequest.builder()
                .email(request.getCustomerEmail())
                .amount(convertToKobo(request.getAmount()))
                .currency(Optional.ofNullable(request.getCurrency()).orElse("NGN"))
                .callback_url(request.getCallbackUrl())
                .reference(generateReference(request.getReference()))
                .metadata(mapMetadata(request.getMetadata()))
                .build();
    }

    public PaymentResponse toPaymentResponse(InitializePaymentResponse response) {
        if (response == null || response.getData() == null) {
            throw new IllegalStateException("Invalid payment response from provider");
        }

        return PaymentResponse.builder()
                .paymentReference(response.getData().getReference())
                .authorizationUrl(response.getData().getAuthorization_url())
                .status(mapStatus(response.isStatus()))
                .providerMessage(response.getMessage())
                .metadata(mapResponseMetadata(response.getData()))
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plus(30, ChronoUnit.MINUTES))
                .build();
    }

    public PaymentRecord toPaymentRecord(PaymentRequest request, InitializePaymentResponse response) {
        return PaymentRecord.builder()
                .id(UUID.randomUUID().toString())
                .customerEmail(request.getCustomerEmail())
                .amount(request.getAmount())
                .currency(Optional.ofNullable(request.getCurrency()).orElse("NGN"))
                .internalReference("INT-" + UUID.randomUUID().toString())
                .providerReference(response.getData().getReference())
                .status(PaymentStatus.PENDING)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .expiresAt(Instant.now().plus(30, ChronoUnit.MINUTES))
                .metadataJson(serializeMetadata(request, response))
                .build();
    }

    private String convertToKobo(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(100)).toBigInteger().toString();
    }

    private String generateReference(String existingReference) {
        return existingReference != null ? existingReference : "PAY-" + UUID.randomUUID();
    }

    private Map<String, Object> mapMetadata(Map<String, String> sourceMetadata) {
        if (sourceMetadata == null) return null;

        return Map.of("custom_fields", sourceMetadata.entrySet().stream()
                .map(entry -> Map.of(
                        "display_name", entry.getKey(),
                        "variable_name", entry.getKey().toLowerCase().replace(" ", "_"),
                        "value", entry.getValue()
                ))
                .collect(Collectors.toList()));
    }

    private PaymentStatus mapStatus(boolean providerStatus) {
        return providerStatus ? PaymentStatus.PENDING : PaymentStatus.FAILED;
    }

    private Map<String, String> mapResponseMetadata(InitializePaymentResponse.PaymentData data) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("access_code", data.getAccess_code());
        metadata.put("provider_reference", data.getReference());
        return metadata;
    }

    private String serializeMetadata(PaymentRequest request, InitializePaymentResponse response) {
        try {
            Map<String, Object> metadata = Map.of(
                    "provider_response", response,
                    "callback_url", request.getCallbackUrl(),
                    "request_metadata", request.getMetadata()
            );
            return objectMapper.writeValueAsString(metadata);
        } catch (JsonProcessingException e) {
            throw new PaymentMappingException("Failed to serialize payment metadata", e);
        }
    }
}
