package com.nisha.construction.lead.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nisha.construction.lead.dto.CreateLeadRequest;
import com.nisha.construction.lead.entity.Lead;
import com.nisha.construction.lead.service.LeadService;

@RestController
@RequestMapping("/api/leads")
@CrossOrigin(origins = "http://localhost:5173")
public class LeadController {

    private final LeadService leadService;

    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @PostMapping
    public Lead createLead(@jakarta.validation.Valid @RequestBody CreateLeadRequest request) {
        return leadService.createLead(request);
    }

    @GetMapping
    public java.util.List<Lead> getAllLeads() {
        return leadService.getAllLeads();
    }

    @GetMapping("/summary")
    public com.nisha.construction.lead.dto.LeadSummaryDto getLeadSummary() {
        return leadService.getLeadSummary();
    }

    @org.springframework.web.bind.annotation.PutMapping("/{id}/status")
    public Lead updateStatus(@org.springframework.web.bind.annotation.PathVariable java.util.UUID id,
            @org.springframework.web.bind.annotation.RequestParam com.nisha.construction.lead.entity.LeadStatus status) {
        return leadService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public org.springframework.http.ResponseEntity<Void> deleteLead(@PathVariable java.util.UUID id) {
        leadService.deleteLead(id);
        return org.springframework.http.ResponseEntity.noContent().build();
    }
}
