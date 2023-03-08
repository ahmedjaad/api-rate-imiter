package com.irembo.api_rate_limiter.service;

import com.irembo.api_rate_limiter.model.RateLimit;
import com.irembo.api_rate_limiter.model.TenantRateLimit;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;
import io.github.bucket4j.local.LocalBucketBuilder;

import java.util.Objects;

public class BucketProviderUtil {
    private static void addRateLimit(LocalBucketBuilder tenantCumulativeBucket, RateLimit rateLimit) {
        if (Objects.nonNull(rateLimit)) {
            Bandwidth limit = Bandwidth.classic(rateLimit.getCapacity(), Refill.intervally(rateLimit.getRefillTokens(), rateLimit.getPeriod()));
            tenantCumulativeBucket.addLimit(limit);
        }
    }
    public static void addTimeWindowRateLimit(LocalBucketBuilder tenantCumulativeBucket, TenantRateLimit tenantRateLimit) {
        addRateLimit(tenantCumulativeBucket, tenantRateLimit.getTimeWindowRateLimit());
    }
    public static void addMonthlyRateLimit(LocalBucketBuilder tenantCumulativeBucket, TenantRateLimit tenantRateLimit) {
        addRateLimit(tenantCumulativeBucket, tenantRateLimit.getMonthlyRateLimit());
    }
}
