package com.irembo.api_ratel_imiter.resource;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
public class AreaResource {
    private String shape;
    private BigDecimal area;
}
