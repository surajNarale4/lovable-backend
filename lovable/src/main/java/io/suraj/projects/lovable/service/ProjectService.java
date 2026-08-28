package io.suraj.projects.lovable.service;

import io.suraj.projects.lovable.dto.project.ProjectRequest;
import io.suraj.projects.lovable.dto.project.ProjectResponse;
import io.suraj.projects.lovable.dto.project.ProjectSummeryResponse;

import java.util.List;

public interface ProjectService {
    List<ProjectSummeryResponse> getUserProjects(String userId);

    ProjectResponse getUserProjectById(Long id ,String userId);

    ProjectResponse createProject(ProjectRequest request , String userId);

    ProjectResponse updateProject(Long id, ProjectRequest request, String userId);

    void softDelete(Long id, String userId);
}
