package com.nisha.construction.lead.service;

import com.nisha.construction.lead.dto.CreateLeadRequest;
import com.nisha.construction.lead.entity.Lead;
import java.util.List;

public interface LeadService {

    Lead createLead(CreateLeadRequest request);

    List<Lead> getAllLeads();

    Lead getLeadById(java.util.UUID id);

    Lead updateStatus(java.util.UUID id, com.nisha.construction.lead.entity.LeadStatus status);

    com.nisha.construction.lead.dto.LeadSummaryDto getLeadSummary();

    void deleteLead(java.util.UUID id);
}
