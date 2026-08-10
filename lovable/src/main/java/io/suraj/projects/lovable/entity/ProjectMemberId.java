package io.suraj.projects.lovable.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class ProjectMemberId {
    private Long projectId;
    private Long userId;
}
