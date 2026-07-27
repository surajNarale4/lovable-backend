package io.suraj.projects.lovable.entity;

import lombok.Data;

import java.time.Instant;

@Data
public class User {

    private Long id;
    private String email;
    private String passwordHash;
    private String name;
    private String avtarUrl;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

}
