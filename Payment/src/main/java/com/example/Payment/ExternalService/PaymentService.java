package com.example.Payment.ExternalService;

import com.example.Payment.Domain.Requests.InitializePaymentRequest;
import com.example.Payment.Domain.Responses.InitializePaymentResponse;
import com.example.Payment.RestHelper.IRestHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.beans.factory.annotation.Value;
import java.util.Map;

@Slf4j
@Service
public class PaymentService implements IPaymentService {

    private final IRestHelper restHelper;
    private  final String baseUrl;
    private final String secretKey;
    private final String initializePaymentPath;


    public PaymentService(IRestHelper restHelper,
                          @Value("${paystack.base-url}") String baseUrl,
                          @Value("${paystack.secret-key}") String secretKey,
                          @Value("${paystack.initialize-payment-path}") String initializePaymentPath) {

        this.restHelper = restHelper;
        this.baseUrl = baseUrl;
        this.secretKey = secretKey;
        this.initializePaymentPath = initializePaymentPath;
    }


    @Override
    public InitializePaymentResponse initializePayment(InitializePaymentRequest request) throws RestClientException
    {
       String url = baseUrl + initializePaymentPath;
        log.info("Initializing payment with url: {}", url);
        Map<String, String> headers = Map.of(
                "Authorization", "Bearer " + secretKey,
                "Content-Type", "application/json"
        );
        log.info("Initializing payment with headers: {}", headers);
        return restHelper.post(url, headers, request, InitializePaymentResponse.class);
    }
}
