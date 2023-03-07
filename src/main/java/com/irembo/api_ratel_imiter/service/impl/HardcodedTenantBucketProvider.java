package com.irembo.api_ratel_imiter.service.impl;

import com.irembo.api_ratel_imiter.model.TenantRateLimit;
import com.irembo.api_ratel_imiter.service.TenantBucketProvider;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.irembo.api_ratel_imiter.service.impl.HardCodedTenantList.tenantRateLimits;

@Component
public class HardcodedTenantBucketProvider implements TenantBucketProvider {
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
