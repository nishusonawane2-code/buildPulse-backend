package com.nisha.construction.inquiry.service;

import com.nisha.construction.inquiry.dto.SubmitInquiryRequest;
import com.nisha.construction.inquiry.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface InquiryService {
    Inquiry submitInquiry(SubmitInquiryRequest request);

    Page<Inquiry> getAllInquiries(Pageable pageable);

    Inquiry getInquiryById(UUID id);
}
