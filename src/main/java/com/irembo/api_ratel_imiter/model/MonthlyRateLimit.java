package com.irembo.api_ratel_imiter.model;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class MonthlyRateLimit extends RateLimit {
    public static RateLimit of (long capacity, long refillTokens){
        return new RateLimit(capacity, refillTokens, Duration.of(1, ChronoUnit.MONTHS));
    }
    private MonthlyRateLimit() {
        super();
    }
}
