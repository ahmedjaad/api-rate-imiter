package com.irembo.api_ratel_imiter.service.impl;

import com.irembo.api_ratel_imiter.model.TenantRateLimit;
import com.irembo.api_ratel_imiter.service.TenantBucketProvider;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import io.github.bucket4j.local.LocalBucketBuilder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.irembo.api_ratel_imiter.service.BucketProviderUtil.addMonthlyRateLimit;
import static com.irembo.api_ratel_imiter.service.BucketProviderUtil.addTimeWindowRateLimit;
import static com.irembo.api_ratel_imiter.service.impl.HardCodedTenantList.tenantRateLimits;

@Service
public class StandAloneTenantBucketProvider implements TenantBucketProvider {
    private static final Map<String, Bucket> cache = new ConcurrentHashMap<>();
    public static final Bandwidth apiServiceWideLimit = Bandwidth.classic(5, Refill.intervally(5, Duration.of(15, ChronoUnit.SECONDS)));

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
}
