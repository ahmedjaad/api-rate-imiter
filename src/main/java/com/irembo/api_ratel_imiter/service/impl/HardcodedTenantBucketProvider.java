package com.irembo.api_ratel_imiter.service.impl;

import com.irembo.api_ratel_imiter.model.TenantRateLimit;
import com.irembo.api_ratel_imiter.service.TenantBucketProvider;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class HardcodedTenantBucketProvider implements TenantBucketProvider {
    private static final List<TenantRateLimit> tenantRateLimits = Arrays.asList(
            TenantRateLimit.builder()
                    .tenantId("irembo")
                    .capacity(1)
                    .refillTokens(1)
                    .period(Duration.of(1, ChronoUnit.MILLIS))
                    .build(),
            TenantRateLimit.builder()
                    .tenantId("google")
                    .capacity(1)
                    .refillTokens(1)
                    .period(Duration.of(2, ChronoUnit.SECONDS))
                    .build(),
            TenantRateLimit.builder()
                    .tenantId("meta")
                    .capacity(1)
                    .refillTokens(1)
                    .period(Duration.of(1, ChronoUnit.MINUTES))
                    .build()
    );
    private static final Map<String, Bucket> cache = new ConcurrentHashMap<>();
    private static final Bandwidth apiServiceWideLimit  = Bandwidth.classic(5, Refill.intervally(5, Duration.of(15, ChronoUnit.SECONDS)));

    static {
        tenantRateLimits.forEach(tenantRateLimit -> cache.put(tenantRateLimit.getTenantId(), newBucket(tenantRateLimit)));
    }

    @Override
    public Bucket getBucketByTenant(TenantRateLimit tenantRateLimit) {
        return cache.get(tenantRateLimit.getTenantId());
    }

    private static Bucket newBucket(TenantRateLimit tenantRateLimit) {
        Bandwidth limit = Bandwidth.classic(tenantRateLimit.getCapacity(), Refill.intervally(tenantRateLimit.getRefillTokens(), tenantRateLimit.getPeriod()));
        return Bucket.builder()
                .addLimit(apiServiceWideLimit)
                .addLimit(limit)
                .build();
    }
}
