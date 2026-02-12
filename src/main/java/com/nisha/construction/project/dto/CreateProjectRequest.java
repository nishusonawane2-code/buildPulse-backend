package com.nisha.construction.project.dto;

import java.time.LocalDate;

import com.nisha.construction.common.entity.City;
import com.nisha.construction.project.enums.ProjectCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateProjectRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Category is required")
    private ProjectCategory category;

    private com.nisha.construction.project.enums.ProjectStatus status;

    private String imageUrl;

    private City city;

    private java.math.BigDecimal budget;
    private LocalDate startDate;
    private LocalDate completedAt;

    private java.util.UUID leadId;
    private String clientName;
    private String clientPhone;
    private String scale;
    private Integer durationMonths;
    private String materials;
    private Integer progressPercentage;
    private String milestones;
    private String siteGallery;
}
