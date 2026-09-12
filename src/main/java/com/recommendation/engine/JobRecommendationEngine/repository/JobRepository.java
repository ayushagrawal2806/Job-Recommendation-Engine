package com.recommendation.engine.JobRecommendationEngine.repository;

import com.recommendation.engine.JobRecommendationEngine.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {
}