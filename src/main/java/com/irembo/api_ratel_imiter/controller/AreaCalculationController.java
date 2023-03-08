package com.irembo.api_ratel_imiter.controller;

import com.irembo.api_ratel_imiter.dto.RectangleDimensionsDto;
import com.irembo.api_ratel_imiter.model.TenantRateLimit;
import com.irembo.api_ratel_imiter.resource.AreaResource;
import com.irembo.api_ratel_imiter.service.TenantBucketProvider;
import io.github.bucket4j.Bucket;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
class AreaCalculationController {
    private final TenantBucketProvider tenantBucketProvider;

    public AreaCalculationController(@Qualifier("redisCacheTenantBucketProvider") TenantBucketProvider tenantBucketProvider) {
        this.tenantBucketProvider = tenantBucketProvider;
    }

    @PostMapping(value = "/api/v1/area/rectangle")
    public ResponseEntity<AreaResource> rectangle(
            @RequestBody @Validated RectangleDimensionsDto dimensions,
            @RequestHeader(value = "X-tenant-id") String tenantId
    ) {
        Bucket tenantBucket = tenantBucketProvider.getBucketByTenant(TenantRateLimit.builder().tenantId(tenantId).build());
        if (tenantBucket.tryConsume(1)) {
            return ResponseEntity
                    .ok(
                            new AreaResource(
                                    "rectangle",
                                    dimensions.getLength().multiply(dimensions.getWidth())
                            )
                    );
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
}
