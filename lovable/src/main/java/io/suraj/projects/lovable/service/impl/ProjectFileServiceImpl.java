package io.suraj.projects.lovable.service.impl;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.suraj.projects.lovable.dto.project.FileContentResponse;
import io.suraj.projects.lovable.dto.project.FileNode;
import io.suraj.projects.lovable.entity.Project;
import io.suraj.projects.lovable.entity.ProjectFile;
import io.suraj.projects.lovable.error.ResourseNotFoundException;
import io.suraj.projects.lovable.mapper.ProjectFIleMapper;
import io.suraj.projects.lovable.repository.ProjectFileRepository;
import io.suraj.projects.lovable.repository.ProjectRepository;
import io.suraj.projects.lovable.service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectFileServiceImpl implements ProjectFileService {

    @Value("${minio.bucket-name}")
    private String projectBucket;

    @Value("${minio.bucket-name}")
    private String BUCKET_NAME;

    private final ProjectFileRepository projectFileRepository;
    private final ProjectRepository projectRespository;
    private final ProjectFIleMapper projectFIleMapper;
    private final MinioClient minioClient;

    @Override
    public List<FileNode> getFileTree(Long projectId) {

      List<ProjectFile> projectFiles = projectFileRepository.findByProjectId(projectId);
        return projectFIleMapper.toFileNodes(projectFiles);
    }

    @Override
    public FileContentResponse getFileContent(Long projectId, String path) {
        String objectName = projectId + "/" + path;
        try (
                InputStream is = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(BUCKET_NAME)
                                .object(objectName)
                                .build())) {

            String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            return new FileContentResponse(path, content);
        } catch (Exception e) {
            log.error("Failed to read file: {}/{}", projectId, path, e);
            throw new RuntimeException("Failed to read file content", e);
        }
    }

    @Override
    public void saveFile(Long projectId, String path, String content) {
        log.info("file path : {}",path);
        log.info("file content : {}",content);

        Project project = projectRespository.findById(projectId).orElseThrow(()->new ResourseNotFoundException("project not found with id "+projectId));
        String cleanPath = path.startsWith("/") ? path.substring(1) : path;
        String objectKey = projectId + "/" + cleanPath;

        try {
            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
            InputStream inputStream = new ByteArrayInputStream(contentBytes);
            // saving the file content
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(projectBucket)
                            .object(objectKey)
                            .stream(inputStream, contentBytes.length, -1)
                            .contentType(determineContentType(path))
                            .build());

            // Saving the metaData
            ProjectFile file = saveFilePath(project,cleanPath);

            file.setUpdatedAt(Instant.now());
            projectFileRepository.save(file);
            log.info("Saved file: {}", objectKey);
        } catch (Exception e) {
            log.error("Failed to save file {}/{}", projectId, cleanPath, e);
            throw new RuntimeException("File save failed", e);
        }

    }

     public ProjectFile saveFilePath(Project project, String filePath){
       ProjectFile projectFile = projectFileRepository.findByProjectIdAndPath(project.getId(),filePath)
                .orElse(
                        ProjectFile.builder()
                                .project(project)
                                .path(filePath)
                                .build()
                );
       return projectFileRepository.save(projectFile);
    }

    private String determineContentType(String path) {
        String type = URLConnection.guessContentTypeFromName(path);
        if (type != null) return type;
        if (path.endsWith(".jsx") || path.endsWith(".ts") || path.endsWith(".tsx")) return "text/javascript";
        if (path.endsWith(".json")) return "application/json";
        if (path.endsWith(".css")) return "text/css";

        return "text/plain";
    }





}
