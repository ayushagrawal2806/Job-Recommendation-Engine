package com.recommendation.engine.JobRecommendationEngine.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

import java.util.List;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @ElementCollection
    @CollectionTable(name = "job_skills", joinColumns = @JoinColumn(name = "job_id"))
    private List<JobSkill> requiredSkills;

    @Column(nullable = false)
    private Double minYearsExperience;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Integer salaryMin;

    @Column(nullable = false)
    private Integer salaryMax;

    @Column(nullable = false)
    private Boolean remoteAllowed;

    @CreationTimestamp
    private Instant createdAt;
}
