package com.nisha.construction.project.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.nisha.construction.common.entity.City;
import com.nisha.construction.project.enums.ProjectCategory;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectResponse {
    private UUID id;
    private String title;
    private String description;
    private ProjectCategory category;
    private com.nisha.construction.project.enums.ProjectStatus status;
    private String imageUrl;
    private City city;
    private java.math.BigDecimal budget;
    private LocalDate startDate;
    private LocalDate completedAt;
    private UUID leadId;
    private String clientName;
    private String clientPhone;
    private String clientEmail;
    private String scale;
    private Integer durationMonths;
    private String materials;
    private Integer progressPercentage;
    private String milestones;
    private String siteGallery;
}
