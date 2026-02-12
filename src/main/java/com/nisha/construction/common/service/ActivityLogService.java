package com.nisha.construction.common.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nisha.construction.common.entity.ActivityLog;
import com.nisha.construction.common.repository.ActivityLogRepository;
import com.nisha.construction.security.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    public void log(String action, String entityType, String entityId, String details) {
        String userEmail = "SYSTEM";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            userEmail = ((User) principal).getEmail();
        } else if (principal instanceof String) {
            userEmail = (String) principal;
        }

        ActivityLog log = ActivityLog.builder()
                .userEmail(userEmail)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .details(details)
                .build();

        activityLogRepository.save(log);
    }

    public List<ActivityLog> getRecentActivities() {
        return activityLogRepository.findFirst10ByOrderByTimestampDesc();
    }
}
