package com.recommendation.engine.JobRecommendationEngine.service;

import com.recommendation.engine.JobRecommendationEngine.dto.JobMatchResult;
import com.recommendation.engine.JobRecommendationEngine.entity.Candidate;
import com.recommendation.engine.JobRecommendationEngine.entity.Job;
import com.recommendation.engine.JobRecommendationEngine.entity.JobSkill;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoringService {

    public JobMatchResult score(Candidate candidate, Job job,
                                Integer skillWeight, Integer experienceWeight,
                                Integer locationWeight, Integer salaryWeight) {


        List<String> mustHaveSkills = job.getRequiredSkills().stream()
                .filter(JobSkill::isMustHave)
                .map(s -> s.getSkill().toLowerCase())
                .toList();

        List<String> candidateSkills = candidate.getSkills().stream()
                .map(String::toLowerCase)
                .toList();

        boolean missingMustHave = mustHaveSkills.stream()
                .anyMatch(skill -> !candidateSkills.contains(skill));

        if (missingMustHave) return null;


        List<String> niceToHaveSkills = job.getRequiredSkills().stream()
                .filter(s -> !s.isMustHave())
                .map(s -> s.getSkill().toLowerCase())
                .toList();

        Integer skillScore;
        if (!niceToHaveSkills.isEmpty()) {
            long matched = niceToHaveSkills.stream()
                    .filter(candidateSkills::contains)
                    .count();
            skillScore = (int) ((matched * skillWeight) / niceToHaveSkills.size());
        } else {
            skillScore = skillWeight;
        }


        Integer experienceScore;
        if (candidate.getYearsOfExperience() >= job.getMinYearsExperience()) {
            experienceScore = experienceWeight;
        } else {
            double ratio = candidate.getYearsOfExperience() / job.getMinYearsExperience();
            experienceScore = (int) (ratio * experienceWeight);
        }


        Integer locationScore;
        if (candidate.getLocation().equalsIgnoreCase(job.getLocation())) {
            locationScore = locationWeight;
        } else if (job.getRemoteAllowed()) {
            locationScore = (int) (locationWeight * 0.66);
        } else {
            locationScore = 0;
        }


        Integer salaryScore;
        if (candidate.getExpectedSalary() <= job.getSalaryMax()) {
            double overlap = job.getSalaryMax() - candidate.getExpectedSalary();
            double range = job.getSalaryMax() - job.getSalaryMin();
            salaryScore = range > 0 ? (int) Math.min(salaryWeight, (overlap / range) * salaryWeight) : salaryWeight;
        } else {
            salaryScore = 0;
        }

        Integer totalScore = skillScore + experienceScore + locationScore + salaryScore;

        return JobMatchResult.builder()
                .job(job)
                .totalScore(totalScore)
                .skillScore(skillScore)
                .experienceScore(experienceScore)
                .locationScore(locationScore)
                .salaryScore(salaryScore)
                .build();
    }


    public JobMatchResult score(Candidate candidate, Job job) {
        return score(candidate, job, 50, 20, 15, 15);
    }
}