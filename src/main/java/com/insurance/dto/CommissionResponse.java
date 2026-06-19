package com.insurance.dto;

import com.insurance.entity.Commission;
import com.insurance.enums.CommissionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CommissionResponse {

    private Long id;
    private String commissionNumber;
    private Long agentId;
    private String agentName;
    private String agentCode;
    private Long policyId;
    private String policyNumber;
    private BigDecimal commissionAmount;
    private BigDecimal premiumAmount;
    private double commissionRate;
    private CommissionStatus status;
    private String branchOffice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommissionResponse from(Commission commission) {
        CommissionResponse r = new CommissionResponse();
        r.setId(commission.getId());
        r.setCommissionNumber(commission.getCommissionNumber());
        r.setCommissionAmount(commission.getCommissionAmount());
        r.setPremiumAmount(commission.getPremiumAmount());
        r.setCommissionRate(commission.getCommissionRate());
        r.setStatus(commission.getStatus());
        r.setBranchOffice(commission.getBranchOffice());
        r.setCreatedAt(commission.getCreatedAt());
        r.setUpdatedAt(commission.getUpdatedAt());
        if (commission.getAgent() != null) {
            r.setAgentId(commission.getAgent().getId());
            r.setAgentName(commission.getAgent().getFullName());
            r.setAgentCode(commission.getAgent().getAgentCode());
        }
        if (commission.getPolicy() != null) {
            r.setPolicyId(commission.getPolicy().getId());
            r.setPolicyNumber(commission.getPolicy().getPolicyNumber());
        }
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCommissionNumber() { return commissionNumber; }
    public void setCommissionNumber(String commissionNumber) { this.commissionNumber = commissionNumber; }
    public Long getAgentId() { return agentId; }
    public void setAgentId(Long agentId) { this.agentId = agentId; }
    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }
    public String getAgentCode() { return agentCode; }
    public void setAgentCode(String agentCode) { this.agentCode = agentCode; }
    public Long getPolicyId() { return policyId; }
    public void setPolicyId(Long policyId) { this.policyId = policyId; }
    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }
    public BigDecimal getCommissionAmount() { return commissionAmount; }
    public void setCommissionAmount(BigDecimal commissionAmount) { this.commissionAmount = commissionAmount; }
    public BigDecimal getPremiumAmount() { return premiumAmount; }
    public void setPremiumAmount(BigDecimal premiumAmount) { this.premiumAmount = premiumAmount; }
    public double getCommissionRate() { return commissionRate; }
    public void setCommissionRate(double commissionRate) { this.commissionRate = commissionRate; }
    public CommissionStatus getStatus() { return status; }
    public void setStatus(CommissionStatus status) { this.status = status; }
    public String getBranchOffice() { return branchOffice; }
    public void setBranchOffice(String branchOffice) { this.branchOffice = branchOffice; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
