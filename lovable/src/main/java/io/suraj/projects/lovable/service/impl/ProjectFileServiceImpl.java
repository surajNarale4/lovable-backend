package io.suraj.projects.lovable.service.impl;

import io.suraj.projects.lovable.dto.project.FileContentResponse;
import io.suraj.projects.lovable.dto.project.FileNode;
import io.suraj.projects.lovable.service.ProjectFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProjectFileServiceImpl implements ProjectFileService {
    @Override
    public List<FileNode> getFileTree(Long projectId, Long userId) {
        return List.of();
    }

    @Override
    public FileContentResponse getFileContent(Long projectId, String path, Long userId) {
        return null;
    }

    @Override
    public void saveFile(Long projectId, String filePath, String fileContent) {
        log.info("file path : {}",filePath);
        log.info("file content : {}",fileContent);
    }
}
