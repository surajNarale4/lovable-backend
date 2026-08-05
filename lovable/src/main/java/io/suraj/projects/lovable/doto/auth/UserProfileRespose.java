package io.suraj.projects.lovable.doto.auth;

public record UserProfileRespose(
      Long id,
      String email,
      String name,
      String avtarUrl
) {
}
