package com.recommendation.engine.JobRecommendationEngine.entity;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobSkill {

    private String skill;

    private boolean mustHave;
}
