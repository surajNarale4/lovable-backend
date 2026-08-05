package io.suraj.projects.lovable.dto.project;

import java.time.Instant;

public record ProjectSummeryResponse (
        Long id,
        String name,
        Instant createAt,
        Instant updatedAt
){
}
