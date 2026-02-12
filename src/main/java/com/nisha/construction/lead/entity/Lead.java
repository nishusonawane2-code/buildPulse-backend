package com.nisha.construction.lead.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.nisha.construction.project.enums.ProjectCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "leads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lead {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String phone;
    private String email;
    private String city;

    @Enumerated(EnumType.STRING)
    private LeadSource source;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private LeadStatus status = LeadStatus.NEW;

    @Enumerated(EnumType.STRING)
    private ProjectCategory category;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
