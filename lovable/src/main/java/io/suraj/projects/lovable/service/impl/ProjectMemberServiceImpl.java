package io.suraj.projects.lovable.service.impl;

import io.suraj.projects.lovable.dto.members.InviteMemberRequest;
import io.suraj.projects.lovable.dto.members.MemberResponse;
import io.suraj.projects.lovable.entity.Project;
import io.suraj.projects.lovable.entity.ProjectMember;
import io.suraj.projects.lovable.entity.ProjectMemberId;
import io.suraj.projects.lovable.mapper.MemberMapper;
import io.suraj.projects.lovable.mapper.ProjectMapper;
import io.suraj.projects.lovable.repository.ProjectMemberRepository;
import io.suraj.projects.lovable.repository.ProjectRepository;
import io.suraj.projects.lovable.repository.UserRepository;
import io.suraj.projects.lovable.service.ProjectMemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import io.suraj.projects.lovable.entity.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final MemberMapper memberMapper;

    @Override
    public List<MemberResponse> getAllProjectMembers(Long userId, Long projectId) {
        Project project=projectRepository.findByAccessbileProjects(userId,projectId).orElseThrow();
        List<MemberResponse> projectMembers = new ArrayList<>();
        projectMembers.add(memberMapper.toMemberMapper(project.getOwner()));
        projectMemberRepository.findByProjectId(projectId).stream()
                .map(members-> projectMembers.add(memberMapper.toMemberMapper(members)))
                .toList();
        return projectMembers;


    }


    public MemberResponse inviteMember(Long userId, Long projectId, InviteMemberRequest request) {
        Project project = projectRepository.findByAccessbileProjects(userId,projectId).orElseThrow();
        /*
         * Users cannot send invitations on behalf of someone else.
         */
        if(!project.getOwner().getId().equals(userId)){
            throw new RuntimeException("This action is not allowed");
        }

        User invitee = userRepository.findByEmail(request.email()).orElseThrow();
        if(invitee.getId().equals(userId)){
            throw new RuntimeException("cannot invite your self");
        }
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, invitee.getId());

        ProjectMember projectMember = ProjectMember.builder()
                .id(projectMemberId)
                .project(project)
                .user(invitee)
                .role(request.role())
                .invitedAt(Instant.now())
                .build();
        projectMemberRepository.save(projectMember);
        return memberMapper.toMemberMapper(projectMember);

    }

    @Override
    public MemberResponse updateMemberRole(Long projectId, Long memberId, InviteMemberRequest request, Long userId) {

        Project project = projectRepository.findByAccessbileProjects(userId,projectId).orElseThrow();
        /*
         * Users cannot send invitations on behalf of someone else.
         */
        if(!project.getOwner().getId().equals(userId)){
            throw new RuntimeException("This action is not allowed");
        }
        ProjectMember projectMember = projectMemberRepository.findById(
                ProjectMemberId.builder()
                        .userId(memberId)
                        .projectId(projectId)
                        .build()
        ).orElseThrow();
        projectMember.setRole(request.role());
        projectMemberRepository.save(projectMember); //saving explicitly as well
       return memberMapper.toMemberMapper(projectMember);
    }

    @Override
    public MemberResponse deleteProjectMember(Long projectId, Long memberId, Long userId) {
        Project project = projectRepository.findByAccessbileProjects(userId,projectId).orElseThrow();
        /*
         * Users cannot send invitations on behalf of someone else.
         */
        if(!project.getOwner().getId().equals(userId)){
            throw new RuntimeException("This action is not allowed");
        }
        if(project.getOwner().getId().equals(memberId)){
            throw new RuntimeException("you can't delete yourself using this api");
        }
        ProjectMemberId projectMemberId=ProjectMemberId.builder()
                .projectId(projectId)
                .userId(memberId)
                .build();
        if(!projectMemberRepository.existsById(projectMemberId))
            throw new RuntimeException("Project is not exists for given member");
        projectMemberRepository.deleteById(projectMemberId);
        return null;
    }
}
