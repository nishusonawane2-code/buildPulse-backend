package com.nisha.construction.estimate.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nisha.construction.estimate.dto.CalculateEstimateRequest;
import com.nisha.construction.estimate.dto.CreateEstimateRequest;
import com.nisha.construction.estimate.dto.EstimateItemRequest;
import com.nisha.construction.estimate.dto.EstimateItemResponse;
import com.nisha.construction.estimate.dto.EstimateResponse;
import com.nisha.construction.estimate.entity.Estimate;
import com.nisha.construction.estimate.entity.EstimateItem;
import com.nisha.construction.estimate.enums.EstimateStatus;
import com.nisha.construction.estimate.repository.EstimateRepository;
import com.nisha.construction.estimate.service.EstimateService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstimateServiceImpl implements EstimateService {

    private final EstimateRepository estimateRepository;
    private final PricingCalculator pricingCalculator;
    private final com.nisha.construction.common.service.ActivityLogService activityLogService;
    private static final BigDecimal TAX_RATE = new BigDecimal("0.10"); // 10% Tax Rate

    @Override
    @Transactional
    public EstimateResponse createEstimate(CreateEstimateRequest request) {
        Estimate estimate = new Estimate();
        estimate.setLeadId(request.getLeadId());
        estimate.setProjectId(request.getProjectId());
        estimate.setStatus(request.getStatus() != null ? request.getStatus() : EstimateStatus.DRAFT);
        estimate.setNotes(request.getNotes());

        // Process Items
        if (request.getItems() != null) {
            for (EstimateItemRequest itemRequest : request.getItems()) {
                EstimateItem item = mapToItemEntity(itemRequest);
                estimate.addItem(item);
            }
        }

        calculateTotals(estimate);
        Estimate savedEstimate = estimateRepository.save(estimate);
        return mapToResponse(savedEstimate);
    }

    @Override
    @Transactional
    public EstimateResponse updateEstimate(UUID id, CreateEstimateRequest request) {
        Estimate estimate = estimateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estimate not found with id: " + id));

        estimate.setLeadId(request.getLeadId());
        estimate.setProjectId(request.getProjectId());
        if (request.getStatus() != null) {
            estimate.setStatus(request.getStatus());
        }
        estimate.setNotes(request.getNotes());

        // Clear existing items and re-add (Full replacement strategy)
        estimate.getItems().clear();
        if (request.getItems() != null) {
            for (EstimateItemRequest itemRequest : request.getItems()) {
                EstimateItem item = mapToItemEntity(itemRequest);
                estimate.addItem(item);
            }
        }

        calculateTotals(estimate);
        Estimate savedEstimate = estimateRepository.save(estimate);
        return mapToResponse(savedEstimate);
    }

    @Override
    public void deleteEstimate(UUID id) {
        Estimate estimate = estimateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estimate not found with id: " + id));
        estimateRepository.delete(estimate);
        activityLogService.log("DELETE_ESTIMATE", "ESTIMATE", id.toString(),
                "Estimate deleted for Lead: " + estimate.getLeadId());
    }

    @Override
    public EstimateResponse getEstimateById(UUID id) {
        Estimate estimate = estimateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estimate not found with id: " + id));
        return mapToResponse(estimate);
    }

    @Override
    public List<EstimateResponse> getAllEstimates() {
        return estimateRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EstimateResponse calculateAndCreateEstimate(CalculateEstimateRequest request) {
        List<EstimateItemRequest> items = pricingCalculator.calculateItems(
                request.getBuiltUpArea(),
                request.getFloors(),
                request.getQuality(),
                request.getCity());

        CreateEstimateRequest createRequest = new CreateEstimateRequest();
        createRequest.setLeadId(request.getLeadId());
        createRequest.setItems(items);
        createRequest.setNotes(
                "Generated via Public Estimator (" + request.getBuiltUpArea() + " sqft, " + request.getQuality() + ")");
        createRequest.setStatus(EstimateStatus.DRAFT);

        return createEstimate(createRequest);
    }

    @Override
    public com.nisha.construction.estimate.dto.EstimateSummaryDto getEstimateSummary() {
        long count = estimateRepository.count();
        BigDecimal totalValue = estimateRepository.sumTotalValue();
        if (totalValue == null) {
            totalValue = BigDecimal.ZERO;
        }

        return com.nisha.construction.estimate.dto.EstimateSummaryDto.builder()
                .totalEstimates(count)
                .totalValue(totalValue)
                .build();
    }

    private void calculateTotals(Estimate estimate) {
        BigDecimal subtotal = BigDecimal.ZERO;

        for (EstimateItem item : estimate.getItems()) {
            // Recalculate line total to ensure data integrity
            BigDecimal lineTotal = item.getUnitPrice().multiply(new BigDecimal(item.getQuantity()));
            item.setLineTotal(lineTotal);
            subtotal = subtotal.add(lineTotal);
        }

        BigDecimal tax = subtotal.multiply(TAX_RATE);
        BigDecimal total = subtotal.add(tax);

        estimate.setSubtotal(subtotal);
        estimate.setTax(tax);
        estimate.setTotal(total);
    }

    private EstimateItem mapToItemEntity(EstimateItemRequest request) {
        return EstimateItem.builder()
                .description(request.getDescription())
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                // lineTotal calculated in calculateTotals
                .lineTotal(BigDecimal.ZERO)
                .build();
    }

    private EstimateResponse mapToResponse(Estimate estimate) {
        List<EstimateItemResponse> itemResponses = estimate.getItems().stream()
                .map(item -> EstimateItemResponse.builder()
                        .id(item.getId())
                        .description(item.getDescription())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .lineTotal(item.getLineTotal())
                        .build())
                .collect(Collectors.toList());

        return EstimateResponse.builder()
                .id(estimate.getId())
                .leadId(estimate.getLeadId())
                .projectId(estimate.getProjectId())
                .status(estimate.getStatus())
                .subtotal(estimate.getSubtotal())
                .tax(estimate.getTax())
                .total(estimate.getTotal())
                .notes(estimate.getNotes())
                .items(itemResponses)
                .createdAt(estimate.getCreatedAt())
                .updatedAt(estimate.getUpdatedAt())
                .build();
    }
}
