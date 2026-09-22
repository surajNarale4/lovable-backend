package io.suraj.projects.lovable.mapper;

import io.suraj.projects.lovable.dto.project.FileNode;
import io.suraj.projects.lovable.entity.ProjectFile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectFIleMapper {

    List<FileNode> toFileNodes(List<ProjectFile> projectFiles);
}

