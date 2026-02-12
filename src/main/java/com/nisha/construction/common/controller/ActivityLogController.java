package com.nisha.construction.common.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nisha.construction.common.entity.ActivityLog;
import com.nisha.construction.common.service.ActivityLogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    @GetMapping
    public ResponseEntity<List<ActivityLog>> getRecentActivities() {
        return ResponseEntity.ok(activityLogService.getRecentActivities());
    }
}
