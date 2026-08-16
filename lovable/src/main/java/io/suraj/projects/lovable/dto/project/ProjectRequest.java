package io.suraj.projects.lovable.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProjectRequest(
       @NotNull @Size(min=3,message = "Project name must be more than 3 characters")
       String name
) {
}
