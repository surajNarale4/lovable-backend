package io.suraj.projects.lovable.repository;

import io.suraj.projects.lovable.entity.ProjectFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectFileRepository extends JpaRepository<ProjectFile,Long> {
    Optional<ProjectFile> findByProjectIdAndPath(Long projectId, String cleanPath);
}
