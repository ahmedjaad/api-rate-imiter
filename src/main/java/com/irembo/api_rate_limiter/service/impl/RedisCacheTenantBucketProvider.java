package com.irembo.api_rate_limiter.service.impl;

import com.irembo.api_rate_limiter.model.TenantRateLimit;
import com.irembo.api_rate_limiter.service.TenantBucketProvider;
import com.irembo.api_rate_limiter.util.Profiles;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

import static com.irembo.api_rate_limiter.service.impl.HardCodedTenantList.tenantRateLimits;

@Service
@Profile(Profiles.DISTRIBUTED)
public class RedisCacheTenantBucketProvider implements TenantBucketProvider {
    private final ProxyManager<String> bucketsProxyManager;

    public RedisCacheTenantBucketProvider(ProxyManager<String> bucketsProxyManager) {
        this.bucketsProxyManager = bucketsProxyManager;
    }

    @Override
    public Bucket getBucketByTenant(TenantRateLimit tenantRateLimit) {
        Supplier<BucketConfiguration> configSupplier = getConfigSupplierForUser(tenantRateLimit.getTenantId());

        return bucketsProxyManager.builder().build(tenantRateLimit.getTenantId(), configSupplier);
    }

    private Supplier<BucketConfiguration> getConfigSupplierForUser(String tenantId) {
        TenantRateLimit tenantRateLimit = tenantRateLimits.stream()
                .filter(foundTenantRateLimit -> foundTenantRateLimit.getTenantId().equals(tenantId))
                .toList()
                .get(0);

        Bandwidth monthlyLimit = Bandwidth.classic(tenantRateLimit.getMonthlyRateLimit().getCapacity(), Refill.intervally(tenantRateLimit.getMonthlyRateLimit().getRefillTokens(), tenantRateLimit.getMonthlyRateLimit().getPeriod()));
        Bandwidth timeWindowLimit = Bandwidth.classic(tenantRateLimit.getTimeWindowRateLimit().getCapacity(), Refill.intervally(tenantRateLimit.getTimeWindowRateLimit().getRefillTokens(), tenantRateLimit.getTimeWindowRateLimit().getPeriod()));
        return () -> (BucketConfiguration.builder()
                .addLimit(monthlyLimit)
                .addLimit(timeWindowLimit)
                .build());
    }
}
