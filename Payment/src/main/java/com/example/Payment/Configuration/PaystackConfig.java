package com.example.Payment.Configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "paystack")
public class PaystackConfig {
    private String baseUrl;
    private String secretKey;
    private String initializePaymentPath;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getInitializePaymentPath() {
        return initializePaymentPath;
    }

    public void setInitializePaymentPath(String initializePaymentPath) {
        this.initializePaymentPath = initializePaymentPath;
    }


}
