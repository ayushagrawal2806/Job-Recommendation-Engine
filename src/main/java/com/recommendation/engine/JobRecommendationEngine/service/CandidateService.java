package com.recommendation.engine.JobRecommendationEngine.service;

import com.recommendation.engine.JobRecommendationEngine.entity.Candidate;
import com.recommendation.engine.JobRecommendationEngine.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;

    public Candidate createCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    public Candidate getCandidateById(UUID id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + id));
    }
}
