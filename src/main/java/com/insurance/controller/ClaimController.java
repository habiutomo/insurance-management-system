package com.insurance.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insurance.dto.ApiResponse;
import com.insurance.dto.ClaimRequest;
import com.insurance.entity.Claim;
import com.insurance.enums.ClaimStatus;
import com.insurance.service.ClaimService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Claim>>> getAllClaims() {
        return ResponseEntity.ok(ApiResponse.ok(claimService.getAllClaims()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Claim>> getClaimById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(claimService.getClaimById(id)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Claim>>> searchClaims(@RequestParam String q) {
        return ResponseEntity.ok(ApiResponse.ok(claimService.searchClaims(q)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Claim>> createClaim(@Valid @RequestBody ClaimRequest request) {
        Claim claim = claimService.createClaim(request);
        return ResponseEntity.ok(ApiResponse.ok("Claim created successfully", claim));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Claim>> updateClaimStatus(
            @PathVariable Long id, @RequestParam ClaimStatus status) {
        Claim claim = claimService.updateClaimStatus(id, status);
        return ResponseEntity.ok(ApiResponse.ok("Claim status updated", claim));
    }

    @GetMapping("/policy/{policyId}")
    public ResponseEntity<ApiResponse<List<Claim>>> getClaimsByPolicy(@PathVariable Long policyId) {
        return ResponseEntity.ok(ApiResponse.ok(claimService.getClaimsByPolicy(policyId)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteClaim(@PathVariable Long id) {
        claimService.deleteClaim(id);
        return ResponseEntity.ok(ApiResponse.ok("Claim deleted successfully", null));
    }
}
