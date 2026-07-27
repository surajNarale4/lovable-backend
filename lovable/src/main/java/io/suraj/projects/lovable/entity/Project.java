package io.suraj.projects.lovable.entity;

import java.time.Instant;

public class Project {
    private Long id;
    private String name;
    private User owner;
    private Boolean isPublic;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}
