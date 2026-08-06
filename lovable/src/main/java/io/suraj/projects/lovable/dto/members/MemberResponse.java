package io.suraj.projects.lovable.dto.members;

import io.suraj.projects.lovable.entity.enums.ProjectRole;

import java.time.Instant;

public record MemberResponse(
        Long id,
        String email,
        String name,
        String avtarUrl,
        ProjectRole role,
        Instant invitedAt
) {
}
