package com.nisha.construction.estimate.service;

import java.util.List;
import java.util.UUID;

import com.nisha.construction.estimate.dto.CreateEstimateRequest;
import com.nisha.construction.estimate.dto.EstimateResponse;

public interface EstimateService {

    EstimateResponse createEstimate(CreateEstimateRequest request);

    EstimateResponse updateEstimate(UUID id, CreateEstimateRequest request);

    void deleteEstimate(UUID id);

    EstimateResponse getEstimateById(UUID id);

    List<EstimateResponse> getAllEstimates();

    EstimateResponse calculateAndCreateEstimate(com.nisha.construction.estimate.dto.CalculateEstimateRequest request);

    com.nisha.construction.estimate.dto.EstimateSummaryDto getEstimateSummary();
}
