package io.suraj.projects.lovable.repository;

import io.suraj.projects.lovable.entity.ProjectMember;
import io.suraj.projects.lovable.entity.ProjectMemberId;
import io.suraj.projects.lovable.entity.enums.ProjectRole;
import lombok.Builder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {
    List<ProjectMember> findByProjectId(Long projectId);
    Optional<ProjectMember> findById(ProjectMemberId projectMemberId);
    Optional<ProjectMember> findByProjectIdAndUserIdAndRole(Long projectId , Long userId, ProjectRole role);
}
