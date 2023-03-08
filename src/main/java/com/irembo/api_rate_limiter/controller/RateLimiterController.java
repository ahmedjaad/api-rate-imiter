package com.irembo.api_rate_limiter.controller;

import com.irembo.api_rate_limiter.model.TenantRateLimit;
import com.irembo.api_rate_limiter.service.ApiService;
import com.irembo.api_rate_limiter.service.TenantBucketProvider;
import io.github.bucket4j.Bucket;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping("/api/v1/**")
class RateLimiterController {
    private final TenantBucketProvider tenantBucketProvider;
    private final ApiService apiService;

    public RateLimiterController(TenantBucketProvider tenantBucketProvider, ApiService apiService) {
        this.tenantBucketProvider = tenantBucketProvider;
        this.apiService = apiService;
    }

    @GetMapping
    public ResponseEntity<Object> getHandler(
            @RequestHeader(value = "X-tenant-id") String tenantId
    ) {
        Bucket tenantBucket = tenantBucketProvider.getBucketByTenant(TenantRateLimit.builder().tenantId(tenantId).build());
        if (tenantBucket.tryConsume(1)) {
            String endpoint = this.getEndpoint();
            Object apiServiceResponse = apiService.getFromApi(endpoint);
            return ResponseEntity.ok(apiServiceResponse);
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    private String getEndpoint() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return requestAttributes.getRequest().getServletPath();
    }
}