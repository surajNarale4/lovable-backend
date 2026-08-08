package io.suraj.projects.lovable.service.impl;

import io.suraj.projects.lovable.dto.members.InviteMemberRequest;
import io.suraj.projects.lovable.dto.members.MemberResponse;
import io.suraj.projects.lovable.entity.ProjectMember;
import io.suraj.projects.lovable.service.ProjectMemberService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectMemberServiceImpl implements ProjectMemberService {
    @Override
    public List<ProjectMember> getAllProjectMembers(Long userId, Long projectId) {
        return List.of();
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
