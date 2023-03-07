package com.irembo.api_ratel_imiter.model;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TenantRateLimit {
    private final  String tenantId;
    private final RateLimit monthlyRateLimit;
    private final RateLimit timeWindoRateLimit;

}
