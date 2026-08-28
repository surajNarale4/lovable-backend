package io.suraj.projects.lovable.dto.members;

import io.suraj.projects.lovable.entity.enums.ProjectRole;
import lombok.Builder;

import java.time.Instant;

@Builder
public record MemberResponse(
        String id,
        String email,
        String name,
        String avtarUrl,
        ProjectRole role,
        Instant invitedAt
) {
}
