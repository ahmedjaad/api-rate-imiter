package com.irembo.api_ratel_imiter.service.impl;

import com.irembo.api_ratel_imiter.model.TenantRateLimit;
import com.irembo.api_ratel_imiter.service.TenantBucketProvider;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

import static com.irembo.api_ratel_imiter.service.impl.HardCodedTenantList.tenantRateLimits;

@Service
public class RedisCacheTenantBucketProvider implements TenantBucketProvider {
    private final ProxyManager<String> bucketsProxyManager;

    public RedisCacheTenantBucketProvider(ProxyManager<String> bucketsProxyManager) {
        this.bucketsProxyManager = bucketsProxyManager;
    }

    @Override
    public Bucket getBucketByTenant(TenantRateLimit tenantRateLimit) {
        Supplier<BucketConfiguration> configSupplier = getConfigSupplierForUser(tenantRateLimit.getTenantId());

        // Does not always create a new bucket, but instead returns the existing one if it exists.
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
