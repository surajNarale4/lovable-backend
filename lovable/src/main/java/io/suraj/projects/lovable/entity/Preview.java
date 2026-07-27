package io.suraj.projects.lovable.entity;

import io.suraj.projects.lovable.entity.enums.PreviewStatus;

import java.time.Instant;

public class Preview {

    private Long id;
    private Project project;
    private String namespace;
    private String podName;
    private String previewUrl;
    private PreviewStatus status;
    private Instant startedAt;
    private Instant termnatedAt;
    private Instant createdAt;
}
