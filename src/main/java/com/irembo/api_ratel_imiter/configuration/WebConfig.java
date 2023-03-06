package com.irembo.api_ratel_imiter.configuration;

import com.irembo.api_ratel_imiter.service.algorithm.RateLimitAlgorithm;
import com.irembo.api_ratel_imiter.service.algorithm.TokenBucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
public class WebConfig {
    @Bean
    public RateLimitAlgorithm tokenBucket() {
        return TokenBucket.of(1000, 25, Duration.of(250, ChronoUnit.MILLIS));
    }
}
