package com.nisha.construction.estimate.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstimateSummaryDto {
    private long totalEstimates;
    private BigDecimal totalValue;
}
