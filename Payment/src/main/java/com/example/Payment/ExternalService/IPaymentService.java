package com.example.Payment.ExternalService;

import com.example.Payment.Domain.Requests.InitializePaymentRequest;
import com.example.Payment.Domain.Responses.InitializePaymentResponse;
import org.springframework.web.client.RestClientException;

public interface IPaymentService {
    InitializePaymentResponse initializePayment(InitializePaymentRequest request) throws RestClientException;
}
