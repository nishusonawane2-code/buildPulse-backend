package com.nisha.construction.lead.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LeadSummaryDto {
    private long totalLeads;
    private long newLeadsToday;
    private long pendingLeads; // Status = NEW
}
