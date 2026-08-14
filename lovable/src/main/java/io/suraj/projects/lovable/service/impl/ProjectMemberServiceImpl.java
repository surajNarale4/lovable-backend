package io.suraj.projects.lovable.service.impl;

import io.suraj.projects.lovable.dto.members.InviteMemberRequest;
import io.suraj.projects.lovable.dto.members.MemberResponse;
import io.suraj.projects.lovable.entity.ProjectMember;
import io.suraj.projects.lovable.repository.ProjectMemberRepository;
import io.suraj.projects.lovable.repository.UserRepository;
import io.suraj.projects.lovable.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public List<ProjectMember> getAllProjectMembers(Long userId, Long projectId) {
        return projectMemberRepository.findAll();
    }

    @Override
    public MemberResponse inviteMember(Long userId, String projectId, InviteMemberRequest request) {

        return null;
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
