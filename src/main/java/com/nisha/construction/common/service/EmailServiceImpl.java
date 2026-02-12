package com.nisha.construction.common.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("BuiltPulse <no-reply@builtpulse.com>");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
            log.info("Email sent successfully to {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }

    @Override
    public void sendHtmlMessage(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("BuiltPulse <no-reply@builtpulse.com>");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            emailSender.send(message);
            log.info("HTML Email sent successfully to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send HTML email to {}: {}", to, e.getMessage());
        }
    }
}
