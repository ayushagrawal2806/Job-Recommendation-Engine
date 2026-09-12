package com.recommendation.engine.JobRecommendationEngine.service;

import com.recommendation.engine.JobRecommendationEngine.dto.CandidateMatchResult;
import com.recommendation.engine.JobRecommendationEngine.dto.JobMatchResult;
import com.recommendation.engine.JobRecommendationEngine.entity.Candidate;
import com.recommendation.engine.JobRecommendationEngine.entity.Job;
import com.recommendation.engine.JobRecommendationEngine.repository.CandidateRepository;
import com.recommendation.engine.JobRecommendationEngine.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final JobRepository jobRepository;
    private final CandidateRepository candidateRepository;
    private final ScoringService scoringService;

    public List<JobMatchResult> getRecommendations(UUID candidateId, Integer limit,
                                                   Integer skillWeight, Integer experienceWeight,
                                                   Integer locationWeight, Integer salaryWeight) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + candidateId));

        List<Job> allJobs = jobRepository.findAll();

        return allJobs.stream()
                .map(job -> scoringService.score(candidate, job, skillWeight, experienceWeight, locationWeight, salaryWeight))
                .filter(result -> result != null)
                .sorted(Comparator.comparingInt(JobMatchResult::getTotalScore).reversed())
                .limit(limit)
                .toList();
    }

    public List<CandidateMatchResult> getBestCandidates(UUID jobId, Integer limit,
                                                        Integer skillWeight, Integer experienceWeight,
                                                        Integer locationWeight, Integer salaryWeight) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));

        List<Candidate> allCandidates = candidateRepository.findAll();

        return allCandidates.stream()
                .map(candidate -> scoringService.scoreCandidate(candidate, job, skillWeight, experienceWeight, locationWeight, salaryWeight))
                .filter(result -> result != null)
                .sorted(Comparator.comparingInt(CandidateMatchResult::getTotalScore).reversed())
                .limit(limit)
                .toList();
    }
}