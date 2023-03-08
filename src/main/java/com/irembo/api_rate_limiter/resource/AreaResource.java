package com.irembo.api_rate_limiter.resource;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
public class AreaResource {
    private String shape;
    private BigDecimal area;
}
