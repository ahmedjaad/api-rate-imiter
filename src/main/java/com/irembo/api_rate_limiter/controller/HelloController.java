package com.irembo.api_rate_limiter.controller;

import com.irembo.api_rate_limiter.model.TenantRateLimit;
import com.irembo.api_rate_limiter.service.TenantBucketProvider;
import io.github.bucket4j.Bucket;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HelloController {
    private final TenantBucketProvider tenantBucketProvider;

    public HelloController(TenantBucketProvider tenantBucketProvider) {
        this.tenantBucketProvider = tenantBucketProvider;
    }

    @GetMapping(value = "/api/v1/hello")
    public ResponseEntity<String> rectangle(
            @RequestHeader(value = "X-tenant-id") String tenantId
    ) {
        Bucket tenantBucket = tenantBucketProvider.getBucketByTenant(TenantRateLimit.builder().tenantId(tenantId).build());
        if (tenantBucket.tryConsume(1)) {
            return ResponseEntity.ok("Hello World");
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
}
