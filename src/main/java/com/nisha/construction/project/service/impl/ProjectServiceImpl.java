package com.nisha.construction.project.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nisha.construction.project.dto.CreateProjectRequest;
import com.nisha.construction.project.dto.ProjectResponse;
import com.nisha.construction.project.entity.Project;
import com.nisha.construction.project.enums.ProjectCategory;
import com.nisha.construction.project.repository.ProjectRepository;
import com.nisha.construction.project.service.ProjectService;
import com.nisha.construction.common.entity.City;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

        private final ProjectRepository projectRepository;
        private final com.nisha.construction.lead.service.LeadService leadService;
        private final com.nisha.construction.common.service.ActivityLogService activityLogService;
        private final com.nisha.construction.security.repository.UserRepository userRepository;

        @Override
        public ProjectResponse createProject(CreateProjectRequest request) {
                Project project = Project.builder()
                                .title(request.getTitle())
                                .description(request.getDescription())
                                .category(request.getCategory())
                                .status(request.getStatus() != null ? request.getStatus()
                                                : com.nisha.construction.project.enums.ProjectStatus.PLANNING)
                                .imageUrl(request.getImageUrl())
                                .city(request.getCity())
                                .budget(request.getBudget())
                                .startDate(request.getStartDate())
                                .completedAt(request.getCompletedAt())
                                .leadId(request.getLeadId())
                                .clientName(request.getClientName())
                                .clientPhone(request.getClientPhone())
                                .scale(request.getScale())
                                .durationMonths(request.getDurationMonths())
                                .materials(request.getMaterials())
                                .progressPercentage(request.getProgressPercentage() != null
                                                ? request.getProgressPercentage()
                                                : 0)
                                .milestones(request.getMilestones())
                                .siteGallery(request.getSiteGallery())
                                .build();

                Project savedProject = projectRepository.save(project);
                activityLogService.log("CREATE_PROJECT", "PROJECT", savedProject.getId().toString(),
                                "Project created: " + savedProject.getTitle());
                return mapToResponse(savedProject);
        }

        @Override
        public ProjectResponse convertLeadToProject(UUID leadId) {
                com.nisha.construction.lead.entity.Lead lead = leadService.getLeadById(leadId);

                Project project = Project.builder()
                                .title("Project for " + lead.getName())
                                .description("Converted from lead. Source: " + lead.getSource())
                                .category(lead.getCategory() != null ? lead.getCategory() : ProjectCategory.RESIDENTIAL)
                                .status(com.nisha.construction.project.enums.ProjectStatus.PLANNING)
                                .city(City.METRO)
                                .leadId(lead.getId())
                                .clientName(lead.getName())
                                .clientPhone(lead.getPhone())
                                .clientEmail(lead.getEmail())
                                .client(userRepository.findByEmail(lead.getEmail()).orElse(null))
                                .build();

                Project savedProject = projectRepository.save(project);

                // Update Lead Status to QUALIFIED (or CONVERTED if we had that status)
                leadService.updateStatus(lead.getId(), com.nisha.construction.lead.entity.LeadStatus.QUALIFIED);

                activityLogService.log("CONVERT_LEAD", "PROJECT", savedProject.getId().toString(),
                                "Lead " + lead.getName() + " converted to Project");

                return mapToResponse(savedProject);
        }

        @Override
        public List<ProjectResponse> getAllProjects() {
                return projectRepository.findAll().stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public ProjectResponse getProjectById(UUID id) {
                Project project = projectRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));
                return mapToResponse(project);
        }

        @Override
        public List<ProjectResponse> getProjectsByCategory(ProjectCategory category) {
                return projectRepository.findByCategory(category).stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
        }

        private ProjectResponse mapToResponse(Project project) {
                return ProjectResponse.builder()
                                .id(project.getId())
                                .title(project.getTitle())
                                .description(project.getDescription())
                                .category(project.getCategory())
                                .status(project.getStatus())
                                .imageUrl(project.getImageUrl())
                                .city(project.getCity())
                                .budget(project.getBudget())
                                .startDate(project.getStartDate())
                                .completedAt(project.getCompletedAt())
                                .leadId(project.getLeadId())
                                .clientEmail(project.getClientEmail())
                                .scale(project.getScale())
                                .durationMonths(project.getDurationMonths())
                                .materials(project.getMaterials())
                                .progressPercentage(project.getProgressPercentage())
                                .milestones(project.getMilestones())
                                .siteGallery(project.getSiteGallery())
                                .build();
        }

        @Override
        public ProjectResponse updateProject(UUID id, CreateProjectRequest request) {
                Project project = projectRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));

                project.setTitle(request.getTitle());
                project.setDescription(request.getDescription());
                project.setCategory(request.getCategory());
                if (request.getStatus() != null)
                        project.setStatus(request.getStatus());
                project.setImageUrl(request.getImageUrl());
                project.setCity(request.getCity());
                project.setBudget(request.getBudget());
                project.setStartDate(request.getStartDate());
                project.setCompletedAt(request.getCompletedAt());
                project.setClientName(request.getClientName());
                project.setClientPhone(request.getClientPhone());
                project.setScale(request.getScale());
                project.setDurationMonths(request.getDurationMonths());
                project.setMaterials(request.getMaterials());
                project.setProgressPercentage(request.getProgressPercentage());
                project.setMilestones(request.getMilestones());
                project.setSiteGallery(request.getSiteGallery());

                Project savedProject = projectRepository.save(project);
                activityLogService.log("UPDATE_PROJECT", "PROJECT", savedProject.getId().toString(),
                                "Project updated: " + savedProject.getTitle());
                return mapToResponse(savedProject);
        }

        @Override
        public List<ProjectResponse> getProjectsForClient(String email) {
                // Try to find by direct client relationship first, then by email snapshot
                List<Project> projects = projectRepository.findByClient_Email(email);
                if (projects.isEmpty()) {
                        projects = projectRepository.findByClientEmail(email);
                }
                return projects.stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public void deleteProject(UUID id) {
                Project project = projectRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));
                projectRepository.delete(project);
                activityLogService.log("DELETE_PROJECT", "PROJECT", id.toString(),
                                "Project deleted: " + project.getTitle());
        }
}
