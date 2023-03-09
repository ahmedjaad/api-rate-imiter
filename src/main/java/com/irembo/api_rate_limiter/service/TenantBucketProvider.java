package com.irembo.api_rate_limiter.service;

import com.irembo.api_rate_limiter.model.TenantRateLimit;
import io.github.bucket4j.Bucket;

/**
 * This interface is the specification for any service that would provide implementation to provide
 * token bucket based on different rules
 * @author Ahmed Ali Rashid
 */
public interface TenantBucketProvider {
    /**
     * Returns a cumulative bucket that combines all the rules applicable to the specified
     * {@link TenantRateLimit}
     * @param tenantRateLimit a business object that represent a valid tenant
     * @return the cumulative bucket that combine all the rules for the tenant
     */
    Bucket getBucketByTenant(TenantRateLimit tenantRateLimit);
}
