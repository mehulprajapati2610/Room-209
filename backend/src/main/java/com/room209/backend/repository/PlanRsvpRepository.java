package com.room209.backend.repository;

import com.room209.backend.entity.PlanRsvp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PlanRsvpRepository extends JpaRepository<PlanRsvp, Long> {
    Optional<PlanRsvp> findByPlanIdAndUserId(Long planId, Long userId);
}
