package io.suraj.projects.lovable.repository;

import io.suraj.projects.lovable.entity.ProjectMember;
import io.suraj.projects.lovable.entity.ProjectMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {
}
