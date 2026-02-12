package com.nisha.construction.lead.service;

import org.springframework.stereotype.Service;

import com.nisha.construction.lead.dto.CreateLeadRequest;
import com.nisha.construction.lead.entity.Lead;
import com.nisha.construction.lead.entity.LeadSource;
import com.nisha.construction.lead.repository.LeadRepository;
import com.nisha.construction.common.service.EmailService;

@Service
public class LeadServiceImpl implements LeadService {

    private final LeadRepository leadRepository;
    private final com.nisha.construction.common.service.ActivityLogService activityLogService;
    private final EmailService emailService;

    public LeadServiceImpl(LeadRepository leadRepository,
            com.nisha.construction.common.service.ActivityLogService activityLogService,
            EmailService emailService) {
        this.leadRepository = leadRepository;
        this.activityLogService = activityLogService;
        this.emailService = emailService;
    }

    @Override
    public Lead createLead(CreateLeadRequest request) {

        Lead lead = Lead.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .city(request.getCity())
                .source(LeadSource.valueOf(request.getSource().toUpperCase()))
                .category(request.getCategory())
                .build();

        Lead savedLead = leadRepository.save(lead);
        activityLogService.log("CREATE_LEAD", "LEAD", savedLead.getId().toString(),
                "Lead created for: " + savedLead.getName());

        sendConfirmationEmail(savedLead);

        return savedLead;
    }

    private void sendConfirmationEmail(Lead lead) {
        String subject = "Consultation Booking Confirmation - BuildPulse";
        String message = String.format(
                "Hi %s,\n\n" +
                        "Thank you for booking a consultation with us!\n\n" +
                        "We’ve received your request and one of our team members will contact you soon to discuss the next steps.\n\n"
                        +
                        "Here are the details we have received:\n" +
                        "• Name: %s\n" +
                        "• Email: %s\n" +
                        "• Phone: %s\n" +
                        "• City: %s\n" +
                        "• Service requested: %s\n" +
                        "• Booking Date: %s\n" +
                        "• Booking Time: %s\n\n" +
                        "Please feel free to reply to this email if you have any questions or need to update your information.\n\n"
                        +
                        "We look forward to speaking with you soon!\n\n" +
                        "Best regards,\n" +
                        "BuildPulse Team\n" +
                        "info-reply@builtpulse.com",
                lead.getName(),
                lead.getName(),
                lead.getEmail(),
                lead.getPhone(),
                lead.getCity(),
                lead.getSource(),
                lead.getCreatedAt().toLocalDate(),
                lead.getCreatedAt().toLocalTime().withNano(0));

        emailService.sendSimpleMessage(lead.getEmail(), subject, message);
    }

    @Override
    public java.util.List<Lead> getAllLeads() {
        return leadRepository.findAll();
    }

    @Override
    public Lead getLeadById(java.util.UUID id) {
        return leadRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Lead not found with id: " + id));
    }

    @Override
    public Lead updateStatus(java.util.UUID id, com.nisha.construction.lead.entity.LeadStatus status) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Lead not found"));
        lead.setStatus(status);
        return leadRepository.save(lead);
    }

    @Override
    public com.nisha.construction.lead.dto.LeadSummaryDto getLeadSummary() {
        long total = leadRepository.count();
        long pending = leadRepository.countByStatus(com.nisha.construction.lead.entity.LeadStatus.NEW);
        long today = leadRepository
                .countByCreatedAtAfter(java.time.LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.DAYS));

        return com.nisha.construction.lead.dto.LeadSummaryDto.builder()
                .totalLeads(total)
                .pendingLeads(pending)
                .newLeadsToday(today)
                .build();
    }

    @Override
    public void deleteLead(java.util.UUID id) {
        Lead lead = getLeadById(id);
        leadRepository.delete(lead);
        activityLogService.log("DELETE_LEAD", "LEAD", id.toString(), "Lead deleted: " + lead.getName());
    }
}
