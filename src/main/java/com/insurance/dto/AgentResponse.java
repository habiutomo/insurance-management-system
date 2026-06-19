package com.insurance.dto;

import com.insurance.entity.Agent;

import java.time.LocalDateTime;

public class AgentResponse {

    private Long id;
    private String agentCode;
    private String fullName;
    private String email;
    private String phone;
    private String branchOffice;
    private String supervisorName;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AgentResponse from(Agent agent) {
        AgentResponse r = new AgentResponse();
        r.setId(agent.getId());
        r.setAgentCode(agent.getAgentCode());
        r.setFullName(agent.getFullName());
        r.setEmail(agent.getEmail());
        r.setPhone(agent.getPhone());
        r.setBranchOffice(agent.getBranchOffice());
        r.setSupervisorName(agent.getSupervisorName());
        r.setActive(agent.isActive());
        r.setCreatedAt(agent.getCreatedAt());
        r.setUpdatedAt(agent.getUpdatedAt());
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAgentCode() { return agentCode; }
    public void setAgentCode(String agentCode) { this.agentCode = agentCode; }
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
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
