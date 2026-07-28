package io.suraj.projects.lovable.entity;

import io.suraj.projects.lovable.entity.enums.MessageRole;

import java.time.Instant;

public class ChatMessage {

    private Long id;
    private Project project;
    private User user;
    private String content;
    private MessageRole role;
    private String toolsCalls;
    private Integer tokenUsed;
    private Instant createdAt;
}
