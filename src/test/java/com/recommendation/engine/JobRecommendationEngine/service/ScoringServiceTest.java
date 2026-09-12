package com.recommendation.engine.JobRecommendationEngine.service;

import com.recommendation.engine.JobRecommendationEngine.dto.JobMatchResult;
import com.recommendation.engine.JobRecommendationEngine.entity.Candidate;
import com.recommendation.engine.JobRecommendationEngine.entity.Job;
import com.recommendation.engine.JobRecommendationEngine.entity.JobSkill;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScoringServiceTest {

    @Autowired
    private ScoringService scoringService;

    private Candidate buildCandidate(List<String> skills, Double experience, String location, Integer salary) {
        return Candidate.builder()
                .id(UUID.randomUUID())
                .name("Test Candidate")
                .skills(skills)
                .yearsOfExperience(experience)
                .location(location)
                .expectedSalary(salary)
                .build();
    }

    private Job buildJob(List<JobSkill> skills, Double minExp, String location, Integer salaryMin, Integer salaryMax, Boolean remote) {
        return Job.builder()
                .id(UUID.randomUUID())
                .title("Test Job")
                .requiredSkills(skills)
                .minYearsExperience(minExp)
                .location(location)
                .salaryMin(salaryMin)
                .salaryMax(salaryMax)
                .remoteAllowed(remote)
                .build();
    }

    @Test
    void shouldReturnNullWhenCandidateMissesMustHaveSkill() {
        Candidate candidate = buildCandidate(List.of("React"), 2.0, "Bangalore", 800000);

        Job job = buildJob(
                List.of(
                        new JobSkill("React", true),
                        new JobSkill("TypeScript", true)
                ),
                1.0, "Bangalore", 700000, 1200000, false
        );

        JobMatchResult result = scoringService.score(candidate, job);
        assertNull(result);
    }

    @Test
    void shouldPenalizeButNotExcludeWhenBelowMinExperience() {
        Candidate candidate = buildCandidate(List.of("React"), 1.0, "Bangalore", 800000);

        Job job = buildJob(
                List.of(new JobSkill("React", true)),
                2.0, "Bangalore", 700000, 1200000, false
        );

        JobMatchResult result = scoringService.score(candidate, job);
        assertNotNull(result);
        assertTrue(result.getExperienceScore() < 20);
    }

    @Test
    void shouldReturnHighScoreForPerfectMatch() {
        Candidate candidate = buildCandidate(List.of("React", "TypeScript", "GraphQL"), 2.0, "Bangalore", 800000);

        Job job = buildJob(
                List.of(
                        new JobSkill("React", true),
                        new JobSkill("TypeScript", true),
                        new JobSkill("GraphQL", false)
                ),
                1.0, "Bangalore", 700000, 1200000, false
        );

        JobMatchResult result = scoringService.score(candidate, job);
        assertNotNull(result);
        assertEquals(50, result.getSkillScore());
        assertEquals(20, result.getExperienceScore());
        assertEquals(15, result.getLocationScore());
        assertTrue(result.getTotalScore() >= 90);
    }

    @Test
    void shouldScoreZeroSalaryWhenJobMaxBelowExpected() {
        Candidate candidate = buildCandidate(List.of("React"), 2.0, "Bangalore", 1500000);

        Job job = buildJob(
                List.of(new JobSkill("React", true)),
                1.0, "Bangalore", 700000, 1000000, false
        );

        JobMatchResult result = scoringService.score(candidate, job);
        assertNotNull(result);
        assertEquals(0, result.getSalaryScore());
    }

    @Test
    void shouldScore9ForRemoteWhenLocationMismatch() {
        Candidate candidate = buildCandidate(List.of("React"), 2.0, "Mumbai", 800000);

        Job job = buildJob(
                List.of(new JobSkill("React", true)),
                1.0, "Bangalore", 700000, 1200000, true
        );

        JobMatchResult result = scoringService.score(candidate, job);
        assertNotNull(result);
        assertEquals(9, result.getLocationScore());
    }

    @Test
    void shouldUseCustomWeightsWhenProvided() {
        Candidate candidate = buildCandidate(List.of("React", "TypeScript", "GraphQL"), 2.0, "Bangalore", 700000);

        Job job = buildJob(
                List.of(
                        new JobSkill("React", true),
                        new JobSkill("TypeScript", true),
                        new JobSkill("GraphQL", false)
                ),
                1.0, "Bangalore", 700000, 1200000, false
        );

        JobMatchResult result = scoringService.score(candidate, job, 70, 10, 10, 10);
        assertNotNull(result);
        assertEquals(70, result.getSkillScore());
        assertEquals(10, result.getExperienceScore());
        assertEquals(10, result.getLocationScore());
        assertEquals(100, result.getTotalScore());
    }

    @Test
    void shouldUseDefaultWeightsWhenNotProvided() {
        Candidate candidate = buildCandidate(List.of("React", "TypeScript", "GraphQL"), 2.0, "Bangalore", 700000);

        Job job = buildJob(
                List.of(
                        new JobSkill("React", true),
                        new JobSkill("TypeScript", true),
                        new JobSkill("GraphQL", false)
                ),
                1.0, "Bangalore", 700000, 1200000, false
        );

        JobMatchResult result = scoringService.score(candidate, job);
        assertNotNull(result);
        assertEquals(50, result.getSkillScore());
        assertEquals(20, result.getExperienceScore());
        assertEquals(15, result.getLocationScore());
        assertEquals(100, result.getTotalScore());
    }

    @Test
    void shouldScoreZeroSkillsWhenSkillWeightIsZero() {
        Candidate candidate = buildCandidate(List.of("React"), 2.0, "Bangalore", 700000);

        Job job = buildJob(
                List.of(
                        new JobSkill("React", true),
                        new JobSkill("GraphQL", false)
                ),
                1.0, "Bangalore", 700000, 1200000, false
        );

        JobMatchResult result = scoringService.score(candidate, job, 0, 20, 15, 15);
        assertNotNull(result);
        assertEquals(0, result.getSkillScore());
        assertEquals(50, result.getTotalScore());
    }
}