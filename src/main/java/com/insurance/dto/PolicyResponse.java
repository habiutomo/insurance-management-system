package com.insurance.dto;

import com.insurance.entity.InsurancePolicy;
import com.insurance.enums.PolicyStatus;
import com.insurance.enums.PolicyType;
import com.insurance.enums.PremiumFrequency;
import com.insurance.enums.PremiumPaymentMethod;
import com.insurance.enums.UnderwritingStatus;

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
    private String insuredName;
    private String beneficiaryName;
    private String beneficiaryRelationship;
    private String bankAccountNumber;
    private String bankAccountName;
    private String bankName;
    private PremiumPaymentMethod premiumPaymentMethod;
    private PremiumFrequency premiumFrequency;
    private UnderwritingStatus underwritingStatus;
    private Boolean autoRenew;
    private String branchOffice;
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
        r.setInsuredName(policy.getInsuredName());
        r.setBeneficiaryName(policy.getBeneficiaryName());
        r.setBeneficiaryRelationship(policy.getBeneficiaryRelationship());
        r.setBankAccountNumber(policy.getBankAccountNumber());
        r.setBankAccountName(policy.getBankAccountName());
        r.setBankName(policy.getBankName());
        r.setPremiumPaymentMethod(policy.getPremiumPaymentMethod());
        r.setPremiumFrequency(policy.getPremiumFrequency());
        r.setUnderwritingStatus(policy.getUnderwritingStatus());
        r.setAutoRenew(policy.isAutoRenew());
        r.setBranchOffice(policy.getBranchOffice());
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
    public String getInsuredName() { return insuredName; }
    public void setInsuredName(String insuredName) { this.insuredName = insuredName; }
    public String getBeneficiaryName() { return beneficiaryName; }
    public void setBeneficiaryName(String beneficiaryName) { this.beneficiaryName = beneficiaryName; }
    public String getBeneficiaryRelationship() { return beneficiaryRelationship; }
    public void setBeneficiaryRelationship(String beneficiaryRelationship) { this.beneficiaryRelationship = beneficiaryRelationship; }
    public String getBankAccountNumber() { return bankAccountNumber; }
    public void setBankAccountNumber(String bankAccountNumber) { this.bankAccountNumber = bankAccountNumber; }
    public String getBankAccountName() { return bankAccountName; }
    public void setBankAccountName(String bankAccountName) { this.bankAccountName = bankAccountName; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public PremiumPaymentMethod getPremiumPaymentMethod() { return premiumPaymentMethod; }
    public void setPremiumPaymentMethod(PremiumPaymentMethod premiumPaymentMethod) { this.premiumPaymentMethod = premiumPaymentMethod; }
    public PremiumFrequency getPremiumFrequency() { return premiumFrequency; }
    public void setPremiumFrequency(PremiumFrequency premiumFrequency) { this.premiumFrequency = premiumFrequency; }
    public UnderwritingStatus getUnderwritingStatus() { return underwritingStatus; }
    public void setUnderwritingStatus(UnderwritingStatus underwritingStatus) { this.underwritingStatus = underwritingStatus; }
    public Boolean getAutoRenew() { return autoRenew; }
    public void setAutoRenew(Boolean autoRenew) { this.autoRenew = autoRenew; }
    public String getBranchOffice() { return branchOffice; }
    public void setBranchOffice(String branchOffice) { this.branchOffice = branchOffice; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
