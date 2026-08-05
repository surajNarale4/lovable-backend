package io.suraj.projects.lovable.dto.auth;

public record UserProfileRespose(
      Long id,
      String email,
      String name,
      String avtarUrl
) {
}
