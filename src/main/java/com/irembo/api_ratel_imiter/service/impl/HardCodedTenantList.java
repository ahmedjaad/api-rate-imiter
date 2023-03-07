package com.irembo.api_ratel_imiter.service.impl;

import com.irembo.api_ratel_imiter.model.TenantRateLimit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

public class HardCodedTenantList {
    protected static final List<TenantRateLimit> tenantRateLimits = Arrays.asList(
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
}
