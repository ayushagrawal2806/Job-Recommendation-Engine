package com.recommendation.engine.JobRecommendationEngine.controller;

import com.recommendation.engine.JobRecommendationEngine.entity.Candidate;
import com.recommendation.engine.JobRecommendationEngine.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @PostMapping
    public ResponseEntity<Candidate> createCandidate(@RequestBody Candidate candidate) {
        return ResponseEntity.status(HttpStatus.CREATED).body(candidateService.createCandidate(candidate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Candidate> getCandidate(@PathVariable UUID id) {
        return ResponseEntity.ok(candidateService.getCandidateById(id));
    }
}
