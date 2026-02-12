package com.nisha.construction.common.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activity_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLog {

    @Id
    @GeneratedValue
    private UUID id;

    private String userEmail;
    private String action; // e.g., "CREATE_LEAD", "UPDATE_ESTIMATE", "DELETE_PROJECT"
    private String details;
    private String entityType; // e.g., "LEAD", "ESTIMATE", "PROJECT"
    private String entityId;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
