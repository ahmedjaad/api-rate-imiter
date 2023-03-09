package com.irembo.api_rate_limiter.service.impl;

import com.irembo.api_rate_limiter.model.TenantRateLimit;
import com.irembo.api_rate_limiter.service.BucketProviderUtil;
import com.irembo.api_rate_limiter.service.TenantBucketProvider;
import com.irembo.api_rate_limiter.util.Profiles;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.local.LocalBucketBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
/**
 * This implementation saves the state of rate limit quotas in-memory, which makes this a stateful service,
 * the purpose of this is to demonstrate where the rate limiter can not be horizontally scaled since
 * each individual instance on rate limiter will be stateful
 *
 * @author Ahmed Ali Rashid
 */

@Service
@Profile(Profiles.STANDALONE)
public class StandaloneTenantBucketProvider implements TenantBucketProvider {
    private static final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    static {
        HardCodedTenantRules.tenantRateLimits.forEach(tenantRateLimit -> cache.put(tenantRateLimit.getTenantId(), newBucket(tenantRateLimit)));
    }

    @Override
    public Bucket getBucketByTenant(TenantRateLimit tenantRateLimit) {
        return cache.get(tenantRateLimit.getTenantId());
    }

    private static Bucket newBucket(TenantRateLimit tenantRateLimit) {
        LocalBucketBuilder tenantCumulativeBucket = Bucket.builder()
                .addLimit(HardCodedTenantRules.apiServiceWideLimit);
        if (Objects.nonNull(tenantRateLimit.getMonthlyRateLimit())) {
            tenantCumulativeBucket.addLimit(BucketProviderUtil.getMonthlyRateLimit(tenantRateLimit));
        }
        if (Objects.nonNull(tenantRateLimit.getTimeWindowRateLimit())) {
            tenantCumulativeBucket.addLimit(BucketProviderUtil.getTimeWindowRateLimit(tenantRateLimit));
        }
        return tenantCumulativeBucket
                .build();
    }
}
