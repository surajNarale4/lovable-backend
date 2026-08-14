package io.suraj.projects.lovable.service.impl;

import io.suraj.projects.lovable.dto.members.InviteMemberRequest;
import io.suraj.projects.lovable.dto.members.MemberResponse;
import io.suraj.projects.lovable.entity.Project;
import io.suraj.projects.lovable.entity.ProjectMember;
import io.suraj.projects.lovable.mapper.MemberMapper;
import io.suraj.projects.lovable.mapper.ProjectMapper;
import io.suraj.projects.lovable.repository.ProjectMemberRepository;
import io.suraj.projects.lovable.repository.ProjectRepository;
import io.suraj.projects.lovable.repository.UserRepository;
import io.suraj.projects.lovable.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import io.suraj.projects.lovable.entity.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final MemberMapper memberMapper;

    @Override
    public List<MemberResponse> getAllProjectMembers(Long userId, Long projectId) {
        Project project=projectRepository.findByAccessbileProjects(userId,projectId).orElseThrow();
        List<MemberResponse> projectMembers = new ArrayList<>();
        projectMembers.add(memberMapper.toMamberMapper(project.getOwner()));
        projectMemberRepository.findByProjectId(projectId).stream()
                .map(members-> projectMembers.add(memberMapper.toMemberMapper(members)))
                .toList();
        return projectMembers;


    }


    public MemberResponse inviteMember(Long userId, Long projectId, InviteMemberRequest request) {
        User user=userRepository.findById(userId).orElseThrow();
        Project project = projectRepository.findById(projectId).orElseThrow();
        ProjectMember projectMember = ProjectMember.builder()
                .project(project)
                .user(user)
                .role(request.role())
                .invitedAt(Instant.now())
                .build();
        projectMemberRepository.save(projectMember);

        return MemberResponse.builder()
                .invitedAt(projectMember.getInvitedAt())
                .avtarUrl(user.getAvtarUrl())
                .name(user.getName())
                .email(user.getEmail())
                .id(user.getId())
                .build();
    }

    @Override
    public MemberResponse updateMemberRole(Long projectId, Long memberId, InviteMemberRequest request, Long userId) {
        return null;
    }

    @Override
    public MemberResponse deleteProjectMember(Long projectId, Long memberId, Long userId) {
        return null;
    }
}
