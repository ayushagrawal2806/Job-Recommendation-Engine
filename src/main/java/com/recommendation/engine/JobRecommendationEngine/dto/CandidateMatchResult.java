package com.recommendation.engine.JobRecommendationEngine.dto;

import com.recommendation.engine.JobRecommendationEngine.entity.Candidate;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateMatchResult {
    private Candidate candidate;
    private Integer totalScore;
    private Integer skillScore;
    private Integer experienceScore;
    private Integer locationScore;
    private Integer salaryScore;
}