package com.insurance.dto;

import com.insurance.entity.InsurancePolicy;
import com.insurance.enums.PolicyStatus;
import com.insurance.enums.PolicyType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PolicyResponse {

    private Long id;
    private String policyNumber;
    private PolicyType policyType;
    private PolicyStatus status;
    private BigDecimal premiumAmount;
    private BigDecimal coverageAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PolicyResponse from(InsurancePolicy policy) {
        PolicyResponse r = new PolicyResponse();
        r.setId(policy.getId());
        r.setPolicyNumber(policy.getPolicyNumber());
        r.setPolicyType(policy.getPolicyType());
        r.setStatus(policy.getStatus());
        r.setPremiumAmount(policy.getPremiumAmount());
        r.setCoverageAmount(policy.getCoverageAmount());
        r.setStartDate(policy.getStartDate());
        r.setEndDate(policy.getEndDate());
        r.setCreatedAt(policy.getCreatedAt());
        r.setUpdatedAt(policy.getUpdatedAt());
        if (policy.getCustomer() != null) {
            r.setCustomerId(policy.getCustomer().getId());
            r.setCustomerName(policy.getCustomer().getFullName());
            r.setCustomerEmail(policy.getCustomer().getEmail());
        }
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }
    public PolicyType getPolicyType() { return policyType; }
    public void setPolicyType(PolicyType policyType) { this.policyType = policyType; }
    public PolicyStatus getStatus() { return status; }
    public void setStatus(PolicyStatus status) { this.status = status; }
    public BigDecimal getPremiumAmount() { return premiumAmount; }
    public void setPremiumAmount(BigDecimal premiumAmount) { this.premiumAmount = premiumAmount; }
    public BigDecimal getCoverageAmount() { return coverageAmount; }
    public void setCoverageAmount(BigDecimal coverageAmount) { this.coverageAmount = coverageAmount; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
