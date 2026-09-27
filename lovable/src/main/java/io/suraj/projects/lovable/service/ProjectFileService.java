package io.suraj.projects.lovable.service;

import io.suraj.projects.lovable.dto.project.FileContentResponse;
import io.suraj.projects.lovable.dto.project.FileNode;
import io.suraj.projects.lovable.entity.Project;
import io.suraj.projects.lovable.entity.ProjectFile;

import java.util.List;


public interface ProjectFileService {
    List<FileNode> getFileTree(Long projectId);

    FileContentResponse getFileContent(Long projectId, String path);
    ProjectFile saveFilePath(Project project, String filePath);

    void saveFile(Long projectId, String filePath, String fileContent);
}
