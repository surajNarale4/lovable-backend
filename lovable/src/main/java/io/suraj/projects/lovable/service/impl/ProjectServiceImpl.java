package io.suraj.projects.lovable.service.impl;

import io.suraj.projects.lovable.dto.project.ProjectRequest;
import io.suraj.projects.lovable.dto.project.ProjectResponse;
import io.suraj.projects.lovable.dto.project.ProjectSummeryResponse;
import io.suraj.projects.lovable.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Override
    public List<ProjectSummeryResponse> getUserProjects(Long userId) {
        return List.of();
    }

    @Override
    public ProjectResponse getUserProjectById(Long userId) {
        return null;
    }

    @Override
    public ProjectResponse createProject(ProjectRequest request) {
        return null;
    }

    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest request, Long userId) {
        return null;
    }

    @Override
    public void softDelete(Long id, Long userId) {

    }
}
