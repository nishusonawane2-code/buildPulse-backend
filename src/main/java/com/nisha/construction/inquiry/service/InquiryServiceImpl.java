package com.nisha.construction.inquiry.service;

import com.nisha.construction.common.service.EmailService;
import com.nisha.construction.inquiry.dto.SubmitInquiryRequest;
import com.nisha.construction.inquiry.entity.Inquiry;
import com.nisha.construction.inquiry.repository.InquiryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {

    private final InquiryRepository inquiryRepository;
    private final EmailService emailService;

    @Override
    public Inquiry submitInquiry(SubmitInquiryRequest request) {
        Inquiry inquiry = Inquiry.builder()
                .name(request.getName())
                .email(request.getEmail())
                .subject(request.getSubject())
                .message(request.getMessage())
                .phone(request.getPhone())
                .build();

        Inquiry savedInquiry = inquiryRepository.save(inquiry);
        sendConfirmationEmail(savedInquiry);
        return savedInquiry;
    }

    @Override
    public Page<Inquiry> getAllInquiries(Pageable pageable) {
        return inquiryRepository.findAll(pageable);
    }

    @Override
    public Inquiry getInquiryById(UUID id) {
        return inquiryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inquiry not found with id: " + id));
    }

    private void sendConfirmationEmail(Inquiry inquiry) {
        String subject = "Thank you for contacting BuildPulse";
        String body = String.format(
                "Dear %s,\n\n" +
                        "Thank you for reaching out to BuildPulse. We have received your message regarding: \"%s\".\n\n"
                        +
                        "Our team will review your inquiry and get back to you shortly.\n\n" +
                        "Subject: %s\n" +
                        "Message Summary: %s\n\n" +
                        "Best regards,\n" +
                        "BuildPulse Support Team\n" +
                        "info-reply@builtpulse.com",
                inquiry.getName(),
                inquiry.getSubject(),
                inquiry.getSubject(),
                inquiry.getMessage().length() > 50 ? inquiry.getMessage().substring(0, 50) + "..."
                        : inquiry.getMessage());

        emailService.sendSimpleMessage(inquiry.getEmail(), subject, body);
    }
}
