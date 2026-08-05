package io.suraj.projects.lovable.service;

import io.suraj.projects.lovable.doto.project.ProjectRequest;
import io.suraj.projects.lovable.doto.project.ProjectResponse;
import io.suraj.projects.lovable.doto.project.ProjectSummeryResponse;

import java.util.List;

public interface ProjectService {
    List<ProjectSummeryResponse> getUserProjects(Long userId);

    ProjectResponse getUserProjectById(Long userId);

    ProjectResponse createProject(ProjectRequest request);

    ProjectResponse updateProject(Long id, ProjectRequest request, Long userId);

    void softDelete(Long id, Long userId);
}
