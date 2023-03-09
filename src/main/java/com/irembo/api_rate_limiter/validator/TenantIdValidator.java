package com.irembo.api_rate_limiter.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class TenantIdValidator implements ConstraintValidator<ValidTenantId, String> {
    @Override
    public boolean isValid(String tenantId, ConstraintValidatorContext context) {
        return validateTenantId(tenantId);
    }

    private boolean validateTenantId(final String tenantId) {
        return Arrays.asList("irembo", "google", "meta").contains(tenantId);
    }
}
