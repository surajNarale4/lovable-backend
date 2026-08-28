package io.suraj.projects.lovable.service;

import io.suraj.projects.lovable.dto.members.InviteMemberRequest;
import io.suraj.projects.lovable.dto.members.MemberResponse;
import io.suraj.projects.lovable.entity.ProjectMember;

import java.util.List;

public interface ProjectMemberService {
    List<MemberResponse> getAllProjectMembers(String userId, Long projectId);

    MemberResponse inviteMember(String userId, Long projectId, InviteMemberRequest request);

    MemberResponse updateMemberRole(Long projectId, String memberId, InviteMemberRequest request, String userId);

    MemberResponse deleteProjectMember(Long projectId, String memberId, String userId);
}
