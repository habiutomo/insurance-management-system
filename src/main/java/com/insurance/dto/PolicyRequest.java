package com.insurance.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.insurance.enums.PolicyType;
import com.insurance.enums.PremiumFrequency;
import com.insurance.enums.PremiumPaymentMethod;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PolicyRequest {

    @NotNull
    private PolicyType policyType;

    @NotNull
    @Positive
    private BigDecimal premiumAmount;

    @NotNull
    @Positive
    private BigDecimal coverageAmount;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private Long customerId;

    private String insuredName;
    private String beneficiaryName;
    private String beneficiaryRelationship;
    private String bankAccountNumber;
    private String bankAccountName;
    private String bankName;
    private PremiumPaymentMethod premiumPaymentMethod;
    private PremiumFrequency premiumFrequency;
    private Boolean autoRenew;
    private String branchOffice;

    public PolicyType getPolicyType() { return policyType; }
    public void setPolicyType(PolicyType policyType) { this.policyType = policyType; }
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
    public Boolean getAutoRenew() { return autoRenew; }
    public void setAutoRenew(Boolean autoRenew) { this.autoRenew = autoRenew; }
    public String getBranchOffice() { return branchOffice; }
    public void setBranchOffice(String branchOffice) { this.branchOffice = branchOffice; }
}
