package com.irembo.api_rate_limiter.service.impl;

import com.irembo.api_rate_limiter.model.MonthlyRateLimit;
import com.irembo.api_rate_limiter.model.RateLimit;
import com.irembo.api_rate_limiter.model.TenantRateLimit;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

/**
 * For the sake of brevity we're hardcoding allowed limits for each tenant, but in a real-world scenario these values would come from an engine rule service or cache
 */
public class HardCodedTenantList {
    public static final Bandwidth apiServiceWideLimit = Bandwidth.classic(5, Refill.intervally(5, Duration.of(15, ChronoUnit.SECONDS)));
    protected static final List<TenantRateLimit> tenantRateLimits = Arrays.asList(
            TenantRateLimit.builder()
                    .tenantId("irembo")
                    .timeWindowRateLimit(
                            RateLimit.of(1, 1, Duration.of(1, ChronoUnit.MILLIS))
                    )
                    .monthlyRateLimit(MonthlyRateLimit.of(100, 100))
                    .build(),
            TenantRateLimit.builder()
                    .tenantId("google")
                    .timeWindowRateLimit(
                            RateLimit.of(1, 1, Duration.of(2, ChronoUnit.SECONDS))
                    )
                    .monthlyRateLimit(MonthlyRateLimit.of(100, 100))
                    .build(),
            TenantRateLimit.builder()
                    .tenantId("meta")
                    .timeWindowRateLimit(
                            RateLimit.of(1, 1, Duration.of(1, ChronoUnit.MINUTES))
                    )
                    .monthlyRateLimit(MonthlyRateLimit.of(100, 100))
                    .build()
    );
}
