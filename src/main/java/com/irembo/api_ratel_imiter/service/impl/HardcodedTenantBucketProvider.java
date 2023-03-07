package com.irembo.api_ratel_imiter.service.impl;

import com.irembo.api_ratel_imiter.model.RateLimit;
import com.irembo.api_ratel_imiter.model.TenantRateLimit;
import com.irembo.api_ratel_imiter.service.TenantBucketProvider;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import io.github.bucket4j.local.LocalBucketBuilder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static com.irembo.api_ratel_imiter.service.impl.HardCodedTenantList.tenantRateLimits;

@Component
public class HardcodedTenantBucketProvider implements TenantBucketProvider {
    private static final Map<String, Bucket> cache = new ConcurrentHashMap<>();
    private static final Bandwidth apiServiceWideLimit = Bandwidth.classic(5, Refill.intervally(5, Duration.of(15, ChronoUnit.SECONDS)));

    static {
        tenantRateLimits.forEach(tenantRateLimit -> cache.put(tenantRateLimit.getTenantId(), newBucket(tenantRateLimit)));
    }

    @Override
    public Bucket getBucketByTenant(TenantRateLimit tenantRateLimit) {
        return cache.get(tenantRateLimit.getTenantId());
    }

    private static Bucket newBucket(TenantRateLimit tenantRateLimit) {
        LocalBucketBuilder tenantCumulativeBucket = Bucket.builder()
                .addLimit(apiServiceWideLimit);
        addMonthlyRateLimit(tenantCumulativeBucket, tenantRateLimit);
        addTimeWindowRateLimit(tenantCumulativeBucket, tenantRateLimit);
        return tenantCumulativeBucket
                .build();
    }

    private static void addRateLimit(LocalBucketBuilder tenantCumulativeBucket, RateLimit rateLimit) {
        if (Objects.nonNull(rateLimit)) {
            Bandwidth limit = Bandwidth.classic(rateLimit.getCapacity(), Refill.intervally(rateLimit.getRefillTokens(), rateLimit.getPeriod()));
            tenantCumulativeBucket.addLimit(limit);
        }
    }
    private static void addTimeWindowRateLimit(LocalBucketBuilder tenantCumulativeBucket, TenantRateLimit tenantRateLimit) {
        addRateLimit(tenantCumulativeBucket, tenantRateLimit.getTimeWindoRateLimit());
    }
    private static void addMonthlyRateLimit(LocalBucketBuilder tenantCumulativeBucket, TenantRateLimit tenantRateLimit) {
        addRateLimit(tenantCumulativeBucket, tenantRateLimit.getMonthlyRateLimit());
    }
}
