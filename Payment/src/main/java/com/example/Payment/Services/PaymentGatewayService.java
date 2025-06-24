package com.example.Payment.Services;

import com.example.Payment.Domain.Enum.ErrorCode;
import com.example.Payment.Domain.Enum.PaymentStatus;
import com.example.Payment.Domain.PaymentRecord;
import com.example.Payment.Domain.PaymentRequest;
import com.example.Payment.Domain.PaymentResponse;
import com.example.Payment.Domain.Requests.InitializePaymentRequest;
import com.example.Payment.Domain.Responses.InitializePaymentResponse;
import com.example.Payment.Exception.PaymentMappingException;
import com.example.Payment.Exception.PaymentProcessingException;
import com.example.Payment.ExternalService.IPaymentService;
import com.example.Payment.Mapper.PaymentMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PaymentGatewayService implements IPaymentGatewayService {

    private final IPaymentService paymentService;
    private final ObjectMapper objectMapper;
    private final PaymentMapper paymentMapper;
    //private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentGatewayService(IPaymentService paymentService,
                                     ObjectMapper objectMapper,
                                     PaymentMapper paymentMapper
                                     /*PaymentRepository paymentRepository*/) {
        this.paymentService = paymentService;
        this.objectMapper = objectMapper;
        this.paymentMapper = paymentMapper;
        //this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        try {
            InitializePaymentRequest externalRequest = paymentMapper.toExternalRequest(request);
            InitializePaymentResponse externalResponse = paymentService.initializePayment(externalRequest);
            PaymentRecord record = paymentMapper.toPaymentRecord(request, externalResponse);
            //paymentRepository.save(record);
            return paymentMapper.toPaymentResponse(externalResponse);
        } catch (IllegalArgumentException e) {
            log.error("Invalid payment request: {}", request, e);
            throw new PaymentProcessingException("Invalid payment request", e, ErrorCode.INVALID_REQUEST);
        } catch (RestClientException e) {
            log.error("Payment service" +
                    " communication failed", e);
            throw new PaymentProcessingException("Payment service unavailable", e, ErrorCode.SERVICE_UNAVAILABLE);
        } catch (PaymentMappingException e) {
            log.error("Payment data mapping failed", e);
            throw new PaymentProcessingException("Payment processing error", e, ErrorCode.MAPPING_ERROR);
        }
    }

    @Override
    public PaymentStatus checkPaymentStatus(String paymentReference) {
        return null;
    }
}