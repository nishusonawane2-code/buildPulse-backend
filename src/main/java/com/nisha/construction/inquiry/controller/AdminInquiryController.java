package com.nisha.construction.inquiry.controller;

import com.nisha.construction.inquiry.entity.Inquiry;
import com.nisha.construction.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/inquiries")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminInquiryController {

    private final InquiryService inquiryService;

    @GetMapping
    public Page<Inquiry> getAllInquiries(Pageable pageable) {
        return inquiryService.getAllInquiries(pageable);
    }

    @GetMapping("/{id}")
    public Inquiry getInquiryById(@PathVariable UUID id) {
        return inquiryService.getInquiryById(id);
    }
}
