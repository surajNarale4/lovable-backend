package io.suraj.projects.lovable.dto.members;

import io.suraj.projects.lovable.entity.enums.ProjectRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InviteMemberRequest(
        String userId,
        @Email(message = "please provide correct email") @NotNull String email,
        @NotNull ProjectRole role
) {
}
