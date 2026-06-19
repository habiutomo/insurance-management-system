package com.insurance.controller;

import com.insurance.dto.ApiResponse;
import com.insurance.dto.CommissionRequest;
import com.insurance.entity.Commission;
import com.insurance.enums.CommissionStatus;
import com.insurance.service.CommissionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commissions")
public class CommissionController {

    private final CommissionService commissionService;

    public CommissionController(CommissionService commissionService) {
        this.commissionService = commissionService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Commission>>> getAllCommissions() {
        return ResponseEntity.ok(ApiResponse.ok(commissionService.getAllCommissions()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Commission>> getCommissionById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(commissionService.getCommissionById(id)));
    }

    @GetMapping("/agent/{agentId}")
    public ResponseEntity<ApiResponse<List<Commission>>> getCommissionsByAgent(@PathVariable Long agentId) {
        return ResponseEntity.ok(ApiResponse.ok(commissionService.getCommissionsByAgent(agentId)));
    }

    @GetMapping("/policy/{policyId}")
    public ResponseEntity<ApiResponse<List<Commission>>> getCommissionsByPolicy(@PathVariable Long policyId) {
        return ResponseEntity.ok(ApiResponse.ok(commissionService.getCommissionsByPolicy(policyId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Commission>> createCommission(@Valid @RequestBody CommissionRequest request) {
        Commission commission = commissionService.createCommission(request);
        return ResponseEntity.ok(ApiResponse.ok("Commission created successfully", commission));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Commission>> updateCommissionStatus(
            @PathVariable Long id, @RequestParam CommissionStatus status) {
        Commission commission = commissionService.updateCommissionStatus(id, status);
        return ResponseEntity.ok(ApiResponse.ok("Commission status updated", commission));
    }
}
