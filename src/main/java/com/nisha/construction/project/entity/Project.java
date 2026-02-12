package com.nisha.construction.project.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.nisha.construction.common.entity.City;
import com.nisha.construction.project.enums.ProjectCategory;
import com.nisha.construction.security.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectCategory category;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private com.nisha.construction.project.enums.ProjectStatus status = com.nisha.construction.project.enums.ProjectStatus.PLANNING;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private City city;

    private java.math.BigDecimal budget;
    private LocalDate startDate;
    private LocalDate completedAt;

    // Link to Origin Lead
    private UUID leadId;

    // Client Details (Snapshot from Lead)
    private String clientName;
    private String clientPhone;
    private String clientEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User client;

    // Technical Blueprint Specifications
    private String scale; // e.g., "12,000 sq.ft"
    private Integer durationMonths; // e.g., 18
    private String materials; // e.g., "T-600 Grade Steel, Low-E Glass"

    // 'Live Build' Progress Tracking
    @Builder.Default
    private Integer progressPercentage = 0; // 0 to 100

    @Column(columnDefinition = "TEXT")
    private String milestones; // Semicolon separated list: "Painting starts Monday;Wiring completion"

    @Column(columnDefinition = "TEXT")
    private String siteGallery; // Comma separated URLs

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
