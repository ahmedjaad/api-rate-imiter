package com.irembo.api_rate_limiter.service.impl;

import com.irembo.api_rate_limiter.service.ApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestApiService implements ApiService {
    private final String apiServiceBaseUrl;
    private final RestTemplate restTemplate;

    public RestApiService(@Value("${api.service.base.url}") String apiServiceBaseUrl, RestTemplateBuilder restTemplateBuilder) {
        this.apiServiceBaseUrl = apiServiceBaseUrl;
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public Object getFromApi(String endpoint) {
        String url = "%s%s".formatted(apiServiceBaseUrl, endpoint);
        return this.restTemplate.getForObject(url, String.class);
    }
}
