package com.recommendation.engine.JobRecommendationEngine.dto;

import com.recommendation.engine.JobRecommendationEngine.entity.Job;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobMatchResult {
    private Job job;
    private Integer totalScore;
    private Integer skillScore;
    private Integer experienceScore;
    private Integer locationScore;
    private Integer salaryScore;
}
