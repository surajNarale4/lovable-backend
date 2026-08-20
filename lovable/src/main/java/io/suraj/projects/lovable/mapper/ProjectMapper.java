package io.suraj.projects.lovable.mapper;

import io.suraj.projects.lovable.dto.project.ProjectResponse;
import io.suraj.projects.lovable.dto.project.ProjectSummeryResponse;
import io.suraj.projects.lovable.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


/*
we have conflict in ProjectResponse due to change in relationship
owner is null corrently for this mapper
 */
@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectResponse toProjectResponse(Project project);
    ProjectSummeryResponse toProjectSummeryResponse(Project project);
}
