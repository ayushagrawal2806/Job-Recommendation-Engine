package com.recommendation.engine.JobRecommendationEngine.repository;

import com.recommendation.engine.JobRecommendationEngine.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CandidateRepository extends JpaRepository<Candidate, UUID> {
}
