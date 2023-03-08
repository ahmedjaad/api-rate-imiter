package com.irembo.api_ratel_imiter.model;

import lombok.*;

import java.time.Duration;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RateLimit {
    private  long capacity;
    private  long refillTokens;
    private Duration period;

}
