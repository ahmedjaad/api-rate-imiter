package com.irembo.api_ratel_imiter.model;


import lombok.Builder;
import lombok.Getter;

import java.time.Duration;

@Builder
@Getter
public class TenantRateLimit {
    private final  String tenantId;
    private  long capacity;
    private  long refillTokens;
    private  Duration period;
}
