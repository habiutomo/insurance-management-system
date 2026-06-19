package com.insurance.dto;

import com.insurance.entity.Claim;
import com.insurance.enums.ClaimStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClaimResponse {

    private Long id;
    private String claimNumber;
    private LocalDate incidentDate;
    private BigDecimal claimAmount;
    private ClaimStatus status;
    private String description;
    private Long policyId;
    private String policyNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ClaimResponse from(Claim claim) {
        ClaimResponse r = new ClaimResponse();
        r.setId(claim.getId());
        r.setClaimNumber(claim.getClaimNumber());
        r.setIncidentDate(claim.getIncidentDate());
        r.setClaimAmount(claim.getClaimAmount());
        r.setStatus(claim.getStatus());
        r.setDescription(claim.getDescription());
        r.setCreatedAt(claim.getCreatedAt());
        r.setUpdatedAt(claim.getUpdatedAt());
        if (claim.getPolicy() != null) {
            r.setPolicyId(claim.getPolicy().getId());
            r.setPolicyNumber(claim.getPolicy().getPolicyNumber());
        }
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getClaimNumber() { return claimNumber; }
    public void setClaimNumber(String claimNumber) { this.claimNumber = claimNumber; }
    public LocalDate getIncidentDate() { return incidentDate; }
    public void setIncidentDate(LocalDate incidentDate) { this.incidentDate = incidentDate; }
    public BigDecimal getClaimAmount() { return claimAmount; }
    public void setClaimAmount(BigDecimal claimAmount) { this.claimAmount = claimAmount; }
    public ClaimStatus getStatus() { return status; }
    public void setStatus(ClaimStatus status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getPolicyId() { return policyId; }
    public void setPolicyId(Long policyId) { this.policyId = policyId; }
    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
