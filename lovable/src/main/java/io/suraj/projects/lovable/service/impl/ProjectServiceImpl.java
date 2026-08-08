package io.suraj.projects.lovable.service.impl;

import io.suraj.projects.lovable.dto.project.ProjectRequest;
import io.suraj.projects.lovable.dto.project.ProjectResponse;
import io.suraj.projects.lovable.dto.project.ProjectSummeryResponse;
import io.suraj.projects.lovable.entity.Project;
import io.suraj.projects.lovable.entity.User;
import io.suraj.projects.lovable.mapper.ProjectMapper;
import io.suraj.projects.lovable.repository.ProjectRepository;
import io.suraj.projects.lovable.repository.UserRepository;
import io.suraj.projects.lovable.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;
    @Override
    public List<ProjectSummeryResponse> getUserProjects(Long userId) {
        User user =  userRepository.findById(userId).orElseThrow();
        List<Project> projects = projectRepository.findByAccessbileProjects(user.getId());
        return projects.stream()
                .map(projectMapper::toProjectSummeryResponse)
                .toList();


    }

    @Override
    public ProjectResponse getUserProjectById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        return null;
    }

    @Override
    public ProjectResponse createProject(ProjectRequest request, Long userId) {
        User user= userRepository.findById(userId).orElseThrow();
        Project project=Project.builder()
                .owner(user)
                .name(request.name())
                .build();
        return projectMapper.toProjectResponse(projectRepository.save(project));


    }

    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest request, Long userId) {
        return null;
    }

    @Override
    public void softDelete(Long id, Long userId) {

    }
}
