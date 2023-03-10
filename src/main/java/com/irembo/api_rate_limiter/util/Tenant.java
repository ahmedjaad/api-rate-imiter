package com.irembo.api_rate_limiter.util;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public enum Tenant {
    IREMBO("irembo"),
    META("meta"),
    GOOGLE("google"),
    ;
    private final String tenantId;

    public String getTenantId() {
        return tenantId;
    }

    Tenant(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return tenantId;
    }

    public static boolean contains(String tenantId) {
        AtomicBoolean validTenant = new AtomicBoolean(false);
        List<Tenant> tenants = Arrays.asList(Tenant.values());
        tenants.stream()
                .map(Tenant::getTenantId)
                .forEach(foundTenantId -> {
                    if (foundTenantId.equals(tenantId)) validTenant.set(true);
                });
        return validTenant.get();
    }
}
