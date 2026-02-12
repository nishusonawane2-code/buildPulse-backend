package com.nisha.construction.lead.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nisha.construction.lead.entity.Lead;

public interface LeadRepository extends JpaRepository<Lead, UUID> {

    long countByStatus(com.nisha.construction.lead.entity.LeadStatus status);

    long countByCreatedAtAfter(java.time.LocalDateTime date);
}
