package com.example.Payment.RestHelper;

import org.springframework.web.client.RestClientException;

import java.util.Map;

public interface IRestHelper {
    <T> T get(String url, Map<String, String> headers, Class<T> responseType) throws RestClientException;
    <T, R> T post(String url, Map<String, String> headers,R requestBody,  Class<T> responseType) throws RestClientException;
    <T, R> T put(String url, Map<String, String> headers, R requestBody, Class<T> responseType) throws RestClientException;
    <T, R> T patch(String url, Map<String, String> headers, R requestBody, Class<T> responseType) throws RestClientException;
    <T> T delete(String url, Map<String, String> headers, Class<T> responseType) throws RestClientException;
}
