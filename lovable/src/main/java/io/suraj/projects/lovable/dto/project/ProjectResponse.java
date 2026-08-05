package io.suraj.projects.lovable.dto.project;

import io.suraj.projects.lovable.dto.auth.UserProfileRespose;

import java.time.Instant;

public record ProjectResponse(
        Long id,
        String name,
        Instant createAt,
        Instant updatedAt,
        UserProfileRespose owner
) {
}
