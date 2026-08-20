package io.suraj.projects.lovable.repository;

import io.suraj.projects.lovable.entity.Project;
import io.suraj.projects.lovable.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {

    /*
    Gethering All Projects with ProjectMember EARER Fetch
     */
    @Query(
            """
            SELECT p from Project p Join ProjectMember pm
                        on pm.project = p
                        where pm.user = :user_id
                        AND pm.role='OWNER'
                        AND p.deletedAt is null
                        ORDeR by p.updatedAt desc
            """
    )
    List<Project> findByAccessbileProjects(@Param("user_id") Long userId);

    @Query(
            """
            SELECT p FROM Project p JOIN FETCH ProjectMember pm 
                        on pm.project = p 
                        where p.id=:project_id
                        AND p.deletedAt is null
            """
    )
    Optional<Project> findByAccessbileProjects(
            @Param("user_id") Long userId,
            @Param("project_id") Long projectId
    );

    /*
    The Below need to check it's working properly or not as per Requirements
     */
    @Query(
            """
            SELECT p from Project p 
                        Join fetch ProjectMember pm 
                        on pm.project = p
                        AND pm.user = :user_id
                        where  p.id=:projectId
                        AND p.deletedAt is null
            """
    )
    Optional<Project> findProjectByUserIdAndProjectId(
            @Param("projectId") Long projectId
            ,@Param("user_id") Long userId);
}
