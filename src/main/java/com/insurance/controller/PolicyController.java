package com.insurance.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insurance.dto.ApiResponse;
import com.insurance.dto.PolicyRenewRequest;
import com.insurance.dto.PolicyRequest;
import com.insurance.entity.InsurancePolicy;
import com.insurance.enums.PolicyStatus;
import com.insurance.enums.PolicyType;
import com.insurance.enums.UnderwritingStatus;
import com.insurance.service.PolicyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<InsurancePolicy>>> getAllPolicies() {
        return ResponseEntity.ok(ApiResponse.ok(policyService.getAllPolicies()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InsurancePolicy>> getPolicyById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(policyService.getPolicyById(id)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<InsurancePolicy>>> searchPolicies(@RequestParam String q) {
        return ResponseEntity.ok(ApiResponse.ok(policyService.searchPolicies(q)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InsurancePolicy>> createPolicy(@Valid @RequestBody PolicyRequest request) {
        InsurancePolicy policy = policyService.createPolicy(request);
        return ResponseEntity.ok(ApiResponse.ok("Policy created successfully", policy));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InsurancePolicy>> updatePolicy(
            @PathVariable Long id, @Valid @RequestBody PolicyRequest request) {
        InsurancePolicy policy = policyService.updatePolicy(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Policy updated successfully", policy));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<InsurancePolicy>> updatePolicyStatus(
            @PathVariable Long id, @RequestParam PolicyStatus status) {
        InsurancePolicy policy = policyService.updatePolicyStatus(id, status);
        return ResponseEntity.ok(ApiResponse.ok("Policy status updated", policy));
    }

    @PostMapping("/{id}/renew")
    public ResponseEntity<ApiResponse<InsurancePolicy>> renewPolicy(
            @PathVariable Long id, @Valid @RequestBody PolicyRenewRequest request) {
        InsurancePolicy policy = policyService.renewPolicy(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Policy renewed successfully", policy));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<InsurancePolicy>>> getPoliciesByCustomer(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(ApiResponse.ok(policyService.getPoliciesByCustomer(customerId)));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<InsurancePolicy>>> getPoliciesByType(
            @PathVariable PolicyType type) {
        return ResponseEntity.ok(ApiResponse.ok(policyService.getPoliciesByType(type)));
    }

    @PatchMapping("/{id}/underwriting")
    public ResponseEntity<ApiResponse<InsurancePolicy>> updateUnderwritingStatus(
            @PathVariable Long id, @RequestParam UnderwritingStatus status) {
        InsurancePolicy policy = policyService.updateUnderwritingStatus(id, status);
        return ResponseEntity.ok(ApiResponse.ok("Underwriting status updated", policy));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePolicy(@PathVariable Long id) {
        policyService.deletePolicy(id);
        return ResponseEntity.ok(ApiResponse.ok("Policy deleted successfully", null));
    }
}
