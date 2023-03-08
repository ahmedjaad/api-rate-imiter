package com.irembo.api_ratel_imiter.model;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class MonthlyRateLimit extends RateLimit {
    public static RateLimit of (long capacity, long refillTokens){
        return RateLimit.of(capacity, refillTokens, Duration.of(30, ChronoUnit.DAYS));
    }
}
