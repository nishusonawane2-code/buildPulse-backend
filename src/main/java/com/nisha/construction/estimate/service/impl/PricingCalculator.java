package com.nisha.construction.estimate.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.nisha.construction.estimate.dto.EstimateItemRequest;

@Component
public class PricingCalculator {

    public List<EstimateItemRequest> calculateItems(Integer area, Integer floors, String quality, String city) {
        List<EstimateItemRequest> items = new ArrayList<>();
        BigDecimal ratePerSqFt = getBaseRate(quality, city);

        // Logic: 60% Material, 40% Labor
        BigDecimal materialRate = ratePerSqFt.multiply(new BigDecimal("0.60"));
        BigDecimal laborRate = ratePerSqFt.multiply(new BigDecimal("0.40"));

        // Material Item
        EstimateItemRequest material = new EstimateItemRequest();
        material.setDescription("Construction Materials (" + quality + " Quality)");
        material.setQuantity(area * floors);
        material.setUnitPrice(materialRate);
        items.add(material);

        // Labor Item
        EstimateItemRequest labor = new EstimateItemRequest();
        labor.setDescription("Labor Charges & Workmanship");
        labor.setQuantity(area * floors);
        labor.setUnitPrice(laborRate);
        items.add(labor);

        return items;
    }

    private BigDecimal getBaseRate(String quality, String city) {
        // Simple base rates
        int base = switch (quality.toUpperCase()) {
            case "PREMIUM" -> 2200;
            case "STANDARD" -> 1800;
            default -> 1500; // BASIC
        };

        // City adjustment (e.g. Metro is 10% more)
        if ("METRO".equalsIgnoreCase(city) || "Nashik".equalsIgnoreCase(city)) {
            base += 200;
        }

        return new BigDecimal(base);
    }
}
