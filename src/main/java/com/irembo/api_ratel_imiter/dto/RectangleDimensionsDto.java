package com.irembo.api_ratel_imiter.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RectangleDimensionsDto {
    @NotNull
    private BigDecimal length;
    @NotNull
    private BigDecimal width;
}
