package com.nisha.construction.estimate.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.nisha.construction.estimate.enums.EstimateStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "estimates")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Estimate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Logical references to other entities
    @Column(name = "lead_id")
    private UUID leadId;

    @Column(name = "project_id")
    private UUID projectId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstimateStatus status;

    @Column(nullable = false)
    private BigDecimal subtotal;

    @Column(nullable = false)
    private BigDecimal tax;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "estimate", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @lombok.ToString.Exclude
    @lombok.EqualsAndHashCode.Exclude
    private List<EstimateItem> items = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Helper method to add item
    public void addItem(EstimateItem item) {
        items.add(item);
        item.setEstimate(this);
    }

    public void removeItem(EstimateItem item) {
        items.remove(item);
        item.setEstimate(null);
    }
}
