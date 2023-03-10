package com.irembo.api_rate_limiter.service.impl;

import com.irembo.api_rate_limiter.model.MonthlyRateLimit;
import com.irembo.api_rate_limiter.model.RateLimit;
import com.irembo.api_rate_limiter.model.TenantRateLimit;
import com.irembo.api_rate_limiter.util.Tenant;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

/**
 * For the sake of brevity we're hardcoding allowed limits for each tenant, but in a real-world scenario these values would come from an engine rule service or cache
 */
public class HardCodedTenantRules {
    public static final String API_SERVICE_WIDE_BUCKET_KEY = "system_wide_limit";
    /**
     * This is the rule that is applicable to the whole system, and every tenant must conform with it in the real world scenario these would be obtained from
     *   a rule engine ( a separate service or a cache )
     */
    public static final Bandwidth apiServiceWideLimit = Bandwidth.classic(5, Refill.intervally(5, Duration.of(15, ChronoUnit.SECONDS)));
    /**
     * Hardcoded Rate limit rules (quotas) for each tenant, again in the real world scenario these would be obtained from
     * a rule engine ( a separate service or a cache )
     */
    protected static final List<TenantRateLimit> tenantRateLimits = Arrays.asList(
            TenantRateLimit.builder()
                    .tenantId(Tenant.IREMBO.getTenantId())
                    .timeWindowRateLimit(
                            RateLimit.of(1, 1, Duration.of(1, ChronoUnit.MILLIS))
                    )
                    .monthlyRateLimit(MonthlyRateLimit.of(100, 100))
                    .build(),
            TenantRateLimit.builder()
                    .tenantId(Tenant.GOOGLE.getTenantId())
                    .timeWindowRateLimit(
                            RateLimit.of(1, 1, Duration.of(2, ChronoUnit.SECONDS))
                    )
                    .monthlyRateLimit(MonthlyRateLimit.of(100, 100))
                    .build(),
            TenantRateLimit.builder()
                    .tenantId(Tenant.META.getTenantId())
                    .timeWindowRateLimit(
                            RateLimit.of(1, 1, Duration.of(1, ChronoUnit.MINUTES))
                    )
                    .monthlyRateLimit(MonthlyRateLimit.of(100, 100))
                    .build()
    );
}
