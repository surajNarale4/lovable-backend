package io.suraj.projects.lovable.repository;

import io.suraj.projects.lovable.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan,Long> {
}
