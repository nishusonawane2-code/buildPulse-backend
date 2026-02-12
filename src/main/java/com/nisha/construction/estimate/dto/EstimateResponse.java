package com.nisha.construction.estimate.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.nisha.construction.estimate.enums.EstimateStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstimateResponse {
    private UUID id;
    private UUID leadId;
    private UUID projectId;
    private EstimateStatus status;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;
    private String notes;
    private List<EstimateItemResponse> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
