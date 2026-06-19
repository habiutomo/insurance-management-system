package com.insurance.service;

import com.insurance.dto.CommissionRequest;
import com.insurance.entity.Agent;
import com.insurance.entity.Commission;
import com.insurance.entity.InsurancePolicy;
import com.insurance.enums.CommissionStatus;
import com.insurance.exception.ResourceNotFoundException;
import com.insurance.repository.CommissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommissionService {

    private final CommissionRepository commissionRepository;
    private final AgentService agentService;
    private final PolicyService policyService;

    public CommissionService(CommissionRepository commissionRepository,
                             AgentService agentService,
                             PolicyService policyService) {
        this.commissionRepository = commissionRepository;
        this.agentService = agentService;
        this.policyService = policyService;
    }

    public List<Commission> getAllCommissions() {
        return commissionRepository.findAll();
    }

    public Commission getCommissionById(Long id) {
        return commissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commission not found with id: " + id));
    }

    public List<Commission> getCommissionsByAgent(Long agentId) {
        return commissionRepository.findByAgentId(agentId);
    }

    public List<Commission> getCommissionsByPolicy(Long policyId) {
        return commissionRepository.findByPolicyId(policyId);
    }

    public Commission createCommission(CommissionRequest request) {
        Agent agent = agentService.getAgentById(request.getAgentId());
        InsurancePolicy policy = policyService.getPolicyById(request.getPolicyId());

        Commission commission = new Commission();
        commission.setCommissionNumber(generateCommissionNumber());
        commission.setAgent(agent);
        commission.setPolicy(policy);
        commission.setCommissionAmount(request.getCommissionAmount());
        commission.setPremiumAmount(policy.getPremiumAmount());
        commission.setCommissionRate(request.getCommissionRate());
        commission.setBranchOffice(agent.getBranchOffice());
        commission.setStatus(CommissionStatus.PENDING);
        return commissionRepository.save(commission);
    }

    public Commission updateCommissionStatus(Long id, CommissionStatus status) {
        Commission commission = getCommissionById(id);
        commission.setStatus(status);
        return commissionRepository.save(commission);
    }

    private String generateCommissionNumber() {
        return "COM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
