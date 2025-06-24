package com.example.Payment.Services;

import com.example.Payment.Domain.Enum.PaymentStatus;
import com.example.Payment.Domain.PaymentRequest;
import com.example.Payment.Domain.PaymentResponse;

public interface IPaymentGatewayService {
    PaymentResponse processPayment(PaymentRequest request);
    PaymentStatus checkPaymentStatus(String paymentReference);
}
