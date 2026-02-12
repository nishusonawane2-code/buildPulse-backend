package com.nisha.construction.estimate.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nisha.construction.estimate.entity.Estimate;

@Repository
public interface EstimateRepository extends JpaRepository<Estimate, UUID> {

    @org.springframework.data.jpa.repository.Query("SELECT SUM(e.total) FROM Estimate e")
    java.math.BigDecimal sumTotalValue();
}
