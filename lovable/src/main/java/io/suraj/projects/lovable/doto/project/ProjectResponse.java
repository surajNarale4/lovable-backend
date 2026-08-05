package io.suraj.projects.lovable.doto.project;

import io.suraj.projects.lovable.doto.auth.UserProfileRespose;

import java.time.Instant;

public record ProjectResponse(
        Long id,
        String name,
        Instant createAt,
        Instant updatedAt,
        UserProfileRespose owner
) {
}
