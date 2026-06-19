package com.insurance.dto;

import jakarta.validation.constraints.NotBlank;

public class AgentRequest {

    @NotBlank
    private String fullName;

    private String email;
    private String phone;
    private String branchOffice;
    private String supervisorName;

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getBranchOffice() { return branchOffice; }
    public void setBranchOffice(String branchOffice) { this.branchOffice = branchOffice; }
    public String getSupervisorName() { return supervisorName; }
    public void setSupervisorName(String supervisorName) { this.supervisorName = supervisorName; }
}
