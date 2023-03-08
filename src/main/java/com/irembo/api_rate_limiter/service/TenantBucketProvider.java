package com.irembo.api_rate_limiter.service;

import com.irembo.api_rate_limiter.model.TenantRateLimit;
import io.github.bucket4j.Bucket;

public interface TenantBucketProvider {
    Bucket getBucketByTenant(TenantRateLimit tenantRateLimit);
}
