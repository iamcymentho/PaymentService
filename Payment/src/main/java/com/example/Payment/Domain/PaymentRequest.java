package com.example.Payment.Domain;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;
//import jakarta.validation.constraints.Positive;


import java.math.BigDecimal;
import java.util.Map;


@Data
public class PaymentRequest {
   // @NotBlank
    private String customerEmail;

    @NotNull
    //@Positive
    private BigDecimal amount;

    private String currency;
    private String reference;
    private String callbackUrl;
    private Map<String, String> metadata;
}
