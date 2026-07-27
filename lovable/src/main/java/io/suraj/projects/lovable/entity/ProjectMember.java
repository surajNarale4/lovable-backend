package io.suraj.projects.lovable.entity;

import io.suraj.projects.lovable.entity.enums.ProjectRole;

import java.time.Instant;

public class ProjectMember {

    private ProjectMemberId id;
    private Project project;
    private User user;
    private ProjectRole role;
    private User invitedBy;
    private Instant invitedAt;
}
