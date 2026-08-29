package io.suraj.projects.lovable.service.impl;

import io.suraj.projects.lovable.dto.project.ProjectRequest;
import io.suraj.projects.lovable.dto.project.ProjectResponse;
import io.suraj.projects.lovable.dto.project.ProjectSummeryResponse;
import io.suraj.projects.lovable.entity.Project;
import io.suraj.projects.lovable.entity.ProjectMember;
import io.suraj.projects.lovable.entity.User;
import io.suraj.projects.lovable.entity.enums.ProjectRole;
import io.suraj.projects.lovable.error.ResourseNotFoundException;
import io.suraj.projects.lovable.mapper.ProjectMapper;
import io.suraj.projects.lovable.repository.ProjectMemberRepository;
import io.suraj.projects.lovable.repository.ProjectRepository;
import io.suraj.projects.lovable.repository.UserRepository;
import io.suraj.projects.lovable.service.ProjectMemberService;
import io.suraj.projects.lovable.service.ProjectService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final Keycloak keycloak;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;
    private final ProjectMemberRepository projectMemberRepository;
    @Override
    public List<ProjectSummeryResponse> getUserProjects(String userId) {
        User user =  userRepository.findById(userId).orElseThrow(()->new ResourseNotFoundException("user not found for given user id"));
        List<Project> projects = projectRepository.findByAccessbileProjects(user.getId());
        return projects.stream()
                .map(projectMapper::toProjectSummeryResponse)
                .toList();


    }

    /*
    Below one is for finding particular project of user
     */
    @Override
    public ProjectResponse getUserProjectById(Long id, String userId) {
      Project project=  projectRepository.findProjectByUserIdAndProjectId(id,userId).orElseThrow(()->new ResourseNotFoundException("no project found for given user id"+ userId));
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse createProject(ProjectRequest request, String userId) {
        User user= userRepository.findById(userId).orElseThrow(()->new ResourseNotFoundException("no user found for given id "+userId));
        Project project=Project.builder()
                .name(request.name())
                .build();
        ProjectMember projectMember = ProjectMember.builder()
                .role(ProjectRole.OWNER)
                .user(user)
                .project(project)
                .build();
        projectMemberRepository.save(projectMember);
//        UserResource userResource=keycloak.realm("").users().get(userId);
//        UserRepresentation userRepresentation =userResource.toRepresentation();
//        userRepresentation.setRealmRoles(List.of(String.valueOf(ProjectRole.OWNER)));
        return projectMapper.toProjectResponse(projectRepository.save(project));


    }

    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest request, String userId) {
        Project project = projectRepository.findProjectByUserIdAndProjectId(id, userId).orElseThrow(()->new ResourseNotFoundException("no project found for given used id "+userId));
        project.setName(request.name());
        projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public void softDelete(Long id, String userId) {
        Project project = projectRepository.findProjectByUserIdAndProjectId(id,userId).orElseThrow(()->new ResourseNotFoundException("no project found for given used id "+userId));
        project.setDeletedAt(Instant.now());
        
    }
}
