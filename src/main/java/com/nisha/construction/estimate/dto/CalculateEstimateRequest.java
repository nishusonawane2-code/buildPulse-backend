package com.nisha.construction.estimate.dto;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CalculateEstimateRequest {

    private UUID leadId;

    @NotNull(message = "Built-up area is required")
    @Min(value = 100, message = "Area must be at least 100 sqft")
    private Integer builtUpArea;

    @Min(value = 1, message = "Floors must be at least 1")
    private Integer floors;

    @NotBlank(message = "Quality is required")
    private String quality; // BASIC, STANDARD, PREMIUM

    @NotBlank(message = "City is required")
    private String city;
}
