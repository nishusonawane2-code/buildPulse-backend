package com.nisha.construction.inquiry.controller;

import com.nisha.construction.inquiry.dto.SubmitInquiryRequest;
import com.nisha.construction.inquiry.entity.Inquiry;
import com.nisha.construction.inquiry.service.InquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inquiries")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class PublicInquiryController {

    private final InquiryService inquiryService;

    @PostMapping
    public Inquiry submitInquiry(@Valid @RequestBody SubmitInquiryRequest request) {
        return inquiryService.submitInquiry(request);
    }
}
