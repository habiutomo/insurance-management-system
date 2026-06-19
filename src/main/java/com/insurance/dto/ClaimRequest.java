package com.insurance.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ClaimRequest {

    @NotNull
    private LocalDate incidentDate;

    @NotNull
    @Positive
    private BigDecimal claimAmount;

    private String description;

    @NotNull
    private Long policyId;

    public LocalDate getIncidentDate() { return incidentDate; }
    public void setIncidentDate(LocalDate incidentDate) { this.incidentDate = incidentDate; }
    public BigDecimal getClaimAmount() { return claimAmount; }
    public void setClaimAmount(BigDecimal claimAmount) { this.claimAmount = claimAmount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getPolicyId() { return policyId; }
    public void setPolicyId(Long policyId) { this.policyId = policyId; }
}
