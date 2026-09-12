package com.recommendation.engine.JobRecommendationEngine.controller;

import com.recommendation.engine.JobRecommendationEngine.dto.CandidateMatchResult;
import com.recommendation.engine.JobRecommendationEngine.dto.JobMatchResult;
import com.recommendation.engine.JobRecommendationEngine.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/candidates/{id}/recommendations")
    public ResponseEntity<List<JobMatchResult>> getRecommendations(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "50") Integer skillWeight,
            @RequestParam(defaultValue = "20") Integer experienceWeight,
            @RequestParam(defaultValue = "15") Integer locationWeight,
            @RequestParam(defaultValue = "15") Integer salaryWeight) {

        return ResponseEntity.ok(recommendationService.getRecommendations(
                id, limit, skillWeight, experienceWeight, locationWeight, salaryWeight));
    }

    @GetMapping("/jobs/{id}/recommendations")
    public ResponseEntity<List<CandidateMatchResult>> getBestCandidates(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "50") Integer skillWeight,
            @RequestParam(defaultValue = "20") Integer experienceWeight,
            @RequestParam(defaultValue = "15") Integer locationWeight,
            @RequestParam(defaultValue = "15") Integer salaryWeight) {

        return ResponseEntity.ok(recommendationService.getBestCandidates(
                id, limit, skillWeight, experienceWeight, locationWeight, salaryWeight));
    }
}