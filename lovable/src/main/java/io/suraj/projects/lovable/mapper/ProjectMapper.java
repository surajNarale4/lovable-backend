package io.suraj.projects.lovable.mapper;

import io.suraj.projects.lovable.dto.project.ProjectResponse;
import io.suraj.projects.lovable.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    public ProjectResponse toProjectResponse(Project project);
}
