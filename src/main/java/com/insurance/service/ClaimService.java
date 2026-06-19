package com.insurance.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.insurance.dto.ClaimRequest;
import com.insurance.entity.Claim;
import com.insurance.entity.InsurancePolicy;
import com.insurance.enums.ClaimStatus;
import com.insurance.exception.BadRequestException;
import com.insurance.exception.ResourceNotFoundException;
import com.insurance.repository.ClaimRepository;

@Service
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final PolicyService policyService;

    public ClaimService(ClaimRepository claimRepository, PolicyService policyService) {
        this.claimRepository = claimRepository;
        this.policyService = policyService;
    }

    public List<Claim> getAllClaims() {
        return claimRepository.findAll();
    }

    public Claim getClaimById(Long id) {
        return claimRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + id));
    }

    public Claim createClaim(ClaimRequest request) {
        InsurancePolicy policy = policyService.getPolicyById(request.getPolicyId());

        if (request.getClaimAmount().compareTo(policy.getCoverageAmount()) > 0) {
            throw new BadRequestException("Claim amount cannot exceed coverage amount");
        }

        Claim claim = new Claim();
        claim.setClaimNumber(generateClaimNumber());
        claim.setIncidentDate(request.getIncidentDate());
        claim.setClaimAmount(request.getClaimAmount());
        claim.setDescription(request.getDescription());
        claim.setPolicy(policy);
        claim.setStatus(ClaimStatus.SUBMITTED);
        return claimRepository.save(claim);
    }

    public Claim updateClaimStatus(Long id, ClaimStatus status) {
        Claim claim = getClaimById(id);
        claim.setStatus(status);
        return claimRepository.save(claim);
    }

    public List<Claim> getClaimsByPolicy(Long policyId) {
        return claimRepository.findByPolicyId(policyId);
    }

    public void deleteClaim(Long id) {
        Claim claim = getClaimById(id);
        claimRepository.delete(claim);
    }

    public List<Claim> searchClaims(String keyword) {
        return claimRepository.search(keyword);
    }

    private String generateClaimNumber() {
        return "CLM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
