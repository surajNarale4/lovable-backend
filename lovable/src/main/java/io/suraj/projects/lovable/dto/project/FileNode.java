package io.suraj.projects.lovable.dto.project;

import java.time.Instant;

public record FileNode(
        String path,
        Instant modifiedAt,
        Long Size,
        String type
) {
}
