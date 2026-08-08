package io.suraj.projects.lovable.repository;

import io.suraj.projects.lovable.entity.Project;
import io.suraj.projects.lovable.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {

    @Query(
            """
            SELECT p from Project p 
                        where p.owner.id=:user_id
                        AND p.deletedAt is null
                        ORDeR by p.updatedAt desc
                     
            """
    )
    List<Project> findByAccessbileProjects(@Param("user_id") Long userId);
}
