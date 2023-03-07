package com.irembo.api_ratel_imiter.service;

import com.irembo.api_ratel_imiter.model.TenantRateLimit;
import io.github.bucket4j.Bucket;

public interface TenantBucketProvider {
    Bucket getBucketByTenant(TenantRateLimit tenantRateLimit);
}
