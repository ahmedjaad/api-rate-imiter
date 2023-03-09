package com.irembo.api_rate_limiter.service.impl;

import com.irembo.api_rate_limiter.model.TenantRateLimit;
import com.irembo.api_rate_limiter.service.BucketProviderUtil;
import com.irembo.api_rate_limiter.service.TenantBucketProvider;
import com.irembo.api_rate_limiter.util.Profiles;
import io.github.bucket4j.*;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * This Implementation for rate limits rules/quotas that are in a distributed environment and use Redis as the
 * distributed cache provider
 * @author Ahmed Ali Rashid
 */

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
        TenantRateLimit tenantRateLimit = HardCodedTenantRules.tenantRateLimits.stream()
                .filter(foundTenantRateLimit -> foundTenantRateLimit.getTenantId().equals(tenantId))
                .toList()
                .get(0);

        ConfigurationBuilder configurationBuilder = BucketConfiguration.builder();
        configurationBuilder.addLimit(HardCodedTenantRules.apiServiceWideLimit);
        if (Objects.nonNull(tenantRateLimit.getMonthlyRateLimit())) {
            configurationBuilder.addLimit(BucketProviderUtil.getMonthlyRateLimit(tenantRateLimit));
        }
        if (Objects.nonNull(tenantRateLimit.getTimeWindowRateLimit())) {
            configurationBuilder.addLimit(BucketProviderUtil.getTimeWindowRateLimit(tenantRateLimit));
        }
        return configurationBuilder::build;
    }
}
