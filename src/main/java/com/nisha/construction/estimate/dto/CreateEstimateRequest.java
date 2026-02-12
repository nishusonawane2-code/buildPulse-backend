package com.nisha.construction.estimate.dto;

import java.util.List;
import java.util.UUID;

import com.nisha.construction.estimate.enums.EstimateStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateEstimateRequest {

    private UUID leadId;
    private UUID projectId;

    private EstimateStatus status;
    private String notes;

    @NotEmpty(message = "Estimate must have at least one item")
    @Valid
    private List<EstimateItemRequest> items;
}
