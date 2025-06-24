package com.example.Payment.RestHelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

@Component
public class RestHelper implements  IRestHelper{

    private final HttpClient httpClient;
    private final ObjectMapper mapper;
    private final Duration duration;

    public RestHelper() {
        this(Duration.ofSeconds(120));
    }

    public RestHelper(Duration timeout) {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(timeout)
                .build();
        this.mapper = new ObjectMapper();
        this.duration = timeout;
    }

    @Override
    public <T> T get(String url, Map<String, String> headers, Class<T> responseType) throws RestClientException {
        HttpRequest request = buildRequest(url, "GET", headers, null);
        return executeRequest(request, responseType);
    }

    @Override
    public <T, R> T post(String url, Map<String, String> headers, R requestBody, Class<T> responseType) throws RestClientException {
        HttpRequest request = buildRequest(url, "POST", headers, requestBody);
        return executeRequest(request, responseType);
    }

    @Override
    public <T, R> T put(String url, Map<String, String> headers, R requestBody, Class<T> responseType) throws RestClientException {
        HttpRequest request = buildRequest(url, "PUT", headers, requestBody);
        return executeRequest(request, responseType);
    }

    @Override
    public <T, R> T patch(String url, Map<String, String> headers, R requestBody, Class<T> responseType) throws RestClientException {
        HttpRequest request = buildRequest(url, "PATCH", headers, requestBody);
        return executeRequest(request, responseType);
    }

    @Override
    public <T> T delete(String url, Map<String, String> headers, Class<T> responseType) throws RestClientException {
        HttpRequest request = buildRequest(url, "DELETE", headers, null);
        return executeRequest(request, responseType);
    }

    private <R> HttpRequest buildRequest(String url, String method, Map<String, String> headers, R requestBody) throws RestClientException {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(duration)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json");

            if (headers != null) {
                headers.forEach(builder::header);
            }

            switch (method) {
                case "GET":
                    return builder.GET().build();
                case "DELETE":
                    return builder.DELETE().build();
                case "POST":
                case "PUT":
                case "PATCH":
                    String body = requestBody != null ? mapper.writeValueAsString(requestBody) : "{}";
                    return builder.method(method, HttpRequest.BodyPublishers.ofString(body)).build();
                default:
                    throw new RestClientException("Unsupported HTTP method: " + method);
            }
        } catch (Exception e) {
            throw new RestClientException("Failed to build HTTP request", e);
        }
    }

    private <T> T executeRequest(HttpRequest request, Class<T> responseType) throws RestClientException {
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new RestClientException("HTTP error with status: " + response.statusCode() +
                        " and body: " + response.body());
            }

            if (responseType == Void.class) {
                return null;
            }

            return mapper.readValue(response.body(), responseType);
        } catch (Exception e) {
            throw new RestClientException("Failed to execute HTTP request", e);
        }
    }
}
