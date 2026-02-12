package com.nisha.construction.project.repository;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nisha.construction.project.entity.Project;
import com.nisha.construction.project.enums.ProjectCategory;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findByCategory(ProjectCategory category);

    List<Project> findByClientEmail(String email);

    List<Project> findByClient_Email(String email);
}
