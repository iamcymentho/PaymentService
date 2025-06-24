package com.example.Payment.Domain.Requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitializePaymentRequest {
    private String email;
    private String amount;
    private String currency;
    private String reference;
    private String callback_url;
    private Map<String, Object> metadata;
}
