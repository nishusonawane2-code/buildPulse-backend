package com.nisha.construction.common.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nisha.construction.common.entity.ActivityLog;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, UUID> {
    List<ActivityLog> findFirst10ByOrderByTimestampDesc();
}
