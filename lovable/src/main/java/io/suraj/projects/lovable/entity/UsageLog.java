package io.suraj.projects.lovable.entity;

import java.time.Instant;

public class UsageLog {
    private Long id;
    private Project project;
    private User user;
    private String action;
    private Integer tokenUsed;
    private Integer durationMs;
    private String metadata;
    private Instant createdAt;
}
