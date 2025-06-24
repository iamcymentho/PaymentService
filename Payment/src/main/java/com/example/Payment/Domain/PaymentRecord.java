package com.example.Payment.Domain;

import com.example.Payment.Domain.Enum.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

// @Entity
@Data
@Builder
public class PaymentRecord {
    @Id
    private String id;
    private String internalReference;
    private String providerReference;
    private String customerEmail;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant expiresAt;

    @Column(columnDefinition = "TEXT")
    private String metadataJson;
}
