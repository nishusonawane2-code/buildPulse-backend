package com.nisha.construction.estimate.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstimateItemResponse {
    private UUID id;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
}
