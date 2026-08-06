package io.suraj.projects.lovable.dto.members;

import io.suraj.projects.lovable.entity.enums.ProjectRole;

public record InviteMemberRequest(
        Long userId,
        String email,
        ProjectRole role
) {
}
