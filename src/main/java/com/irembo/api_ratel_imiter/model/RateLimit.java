package com.irembo.api_ratel_imiter.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(staticName = "of")
@Getter
public class RateLimit {
    private  final long capacity;
    private  final long refillTokens;
    private final Duration period;

}
