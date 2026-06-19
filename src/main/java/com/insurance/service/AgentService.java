package com.insurance.service;

import com.insurance.dto.AgentRequest;
import com.insurance.entity.Agent;
import com.insurance.exception.BadRequestException;
import com.insurance.exception.ResourceNotFoundException;
import com.insurance.repository.AgentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AgentService {

    private final AgentRepository agentRepository;

    public AgentService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    public List<Agent> getAllAgents() {
        return agentRepository.findAll();
    }

    public List<Agent> getActiveAgents() {
        return agentRepository.findByActiveTrue();
    }

    public Agent getAgentById(Long id) {
        return agentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agent not found with id: " + id));
    }

    public Agent createAgent(AgentRequest request) {
        Agent agent = new Agent();
        agent.setAgentCode(generateAgentCode());
        agent.setFullName(request.getFullName());
        agent.setEmail(request.getEmail());
        agent.setPhone(request.getPhone());
        agent.setBranchOffice(request.getBranchOffice());
        agent.setSupervisorName(request.getSupervisorName());
        return agentRepository.save(agent);
    }

    public Agent updateAgent(Long id, AgentRequest request) {
        Agent agent = getAgentById(id);
        agent.setFullName(request.getFullName());
        agent.setEmail(request.getEmail());
        agent.setPhone(request.getPhone());
        agent.setBranchOffice(request.getBranchOffice());
        agent.setSupervisorName(request.getSupervisorName());
        return agentRepository.save(agent);
    }

    public void toggleAgentStatus(Long id) {
        Agent agent = getAgentById(id);
        agent.setActive(!agent.isActive());
        agentRepository.save(agent);
    }

    public void deleteAgent(Long id) {
        Agent agent = getAgentById(id);
        agentRepository.delete(agent);
    }

    private String generateAgentCode() {
        return "AGT-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
