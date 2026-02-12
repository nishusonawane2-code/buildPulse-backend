package com.nisha.construction.project.service;

import java.util.List;
import java.util.UUID;

import com.nisha.construction.project.dto.CreateProjectRequest;
import com.nisha.construction.project.dto.ProjectResponse;
import com.nisha.construction.project.enums.ProjectCategory;

public interface ProjectService {
    ProjectResponse createProject(CreateProjectRequest request);

    ProjectResponse convertLeadToProject(java.util.UUID leadId);

    List<ProjectResponse> getAllProjects();

    ProjectResponse getProjectById(UUID id);

    List<ProjectResponse> getProjectsByCategory(ProjectCategory category);

    ProjectResponse updateProject(UUID id, CreateProjectRequest request);

    List<ProjectResponse> getProjectsForClient(String email);

    void deleteProject(UUID id);
}
