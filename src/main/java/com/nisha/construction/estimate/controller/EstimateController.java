package com.nisha.construction.estimate.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nisha.construction.estimate.dto.CreateEstimateRequest;
import com.nisha.construction.estimate.dto.EstimateResponse;
import com.nisha.construction.estimate.service.EstimateService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/estimates")
@RequiredArgsConstructor
public class EstimateController {

    private final EstimateService estimateService;

    @PostMapping
    public ResponseEntity<EstimateResponse> createEstimate(@Valid @RequestBody CreateEstimateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estimateService.createEstimate(request));
    }

    @PostMapping("/calculate")
    public ResponseEntity<EstimateResponse> calculateEstimate(
            @Valid @RequestBody com.nisha.construction.estimate.dto.CalculateEstimateRequest request) {
        return ResponseEntity.ok(estimateService.calculateAndCreateEstimate(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstimateResponse> updateEstimate(@PathVariable UUID id,
            @Valid @RequestBody CreateEstimateRequest request) {
        return ResponseEntity.ok(estimateService.updateEstimate(id, request));
    }

    @DeleteMapping("/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEstimate(@PathVariable UUID id) {
        estimateService.deleteEstimate(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstimateResponse> getEstimateById(@PathVariable UUID id) {
        return ResponseEntity.ok(estimateService.getEstimateById(id));
    }

    @GetMapping
    public ResponseEntity<List<EstimateResponse>> getAllEstimates() {
        return ResponseEntity.ok(estimateService.getAllEstimates());
    }

    @GetMapping("/summary")
    public ResponseEntity<com.nisha.construction.estimate.dto.EstimateSummaryDto> getEstimateSummary() {
        return ResponseEntity.ok(estimateService.getEstimateSummary());
    }
}
