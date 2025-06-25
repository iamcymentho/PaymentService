package com.example.Payment.Controller;

import com.example.Payment.ApiResponse.ApiResponse;
import com.example.Payment.Domain.PaymentRequest;
import com.example.Payment.Domain.PaymentResponse;
import com.example.Payment.Services.PaymentGatewayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class PaymentController {
    private final PaymentGatewayService paymentGatewayService;

    public PaymentController(PaymentGatewayService paymentGatewayService) {
        this.paymentGatewayService = paymentGatewayService;
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<PaymentResponse>> createPayment(@RequestBody PaymentRequest paymentRequest) {
        PaymentResponse response = paymentGatewayService.processPayment(paymentRequest);
        return ResponseEntity.ok(ApiResponse.success(response));

    }
}
