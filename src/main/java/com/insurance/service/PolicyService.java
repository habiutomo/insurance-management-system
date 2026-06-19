package com.insurance.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import com.insurance.dto.PolicyRenewRequest;
import com.insurance.dto.PolicyRequest;
import com.insurance.entity.Customer;
import com.insurance.entity.InsurancePolicy;
import com.insurance.enums.PolicyStatus;
import com.insurance.enums.PolicyType;
import com.insurance.exception.BadRequestException;
import com.insurance.exception.ResourceNotFoundException;
import com.insurance.repository.PolicyRepository;

@Service
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final CustomerService customerService;

    public PolicyService(PolicyRepository policyRepository, CustomerService customerService) {
        this.policyRepository = policyRepository;
        this.customerService = customerService;
    }

    public List<InsurancePolicy> getAllPolicies() {
        return policyRepository.findAll();
    }

    public InsurancePolicy getPolicyById(Long id) {
        return policyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + id));
    }

    public InsurancePolicy createPolicy(PolicyRequest request) {
        Customer customer = customerService.getCustomerById(request.getCustomerId());

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BadRequestException("End date must be after start date");
        }

        InsurancePolicy policy = new InsurancePolicy();
        policy.setPolicyNumber(generatePolicyNumber());
        policy.setPolicyType(request.getPolicyType());
        policy.setPremiumAmount(request.getPremiumAmount());
        policy.setCoverageAmount(request.getCoverageAmount());
        policy.setStartDate(request.getStartDate());
        policy.setEndDate(request.getEndDate());
        policy.setCustomer(customer);
        policy.setStatus(PolicyStatus.ACTIVE);
        return policyRepository.save(policy);
    }

    public InsurancePolicy updatePolicy(Long id, PolicyRequest request) {
        InsurancePolicy policy = getPolicyById(id);
        Customer customer = customerService.getCustomerById(request.getCustomerId());

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BadRequestException("End date must be after start date");
        }

        policy.setPolicyType(request.getPolicyType());
        policy.setPremiumAmount(request.getPremiumAmount());
        policy.setCoverageAmount(request.getCoverageAmount());
        policy.setStartDate(request.getStartDate());
        policy.setEndDate(request.getEndDate());
        policy.setCustomer(customer);
        return policyRepository.save(policy);
    }

    public void deletePolicy(Long id) {
        InsurancePolicy policy = getPolicyById(id);
        policyRepository.delete(policy);
    }

    public InsurancePolicy updatePolicyStatus(Long id, PolicyStatus status) {
        InsurancePolicy policy = getPolicyById(id);
        policy.setStatus(status);
        return policyRepository.save(policy);
    }

    public List<InsurancePolicy> getPoliciesByCustomer(Long customerId) {
        return policyRepository.findByCustomerId(customerId);
    }

    public List<InsurancePolicy> getPoliciesByType(PolicyType type) {
        return policyRepository.findByPolicyType(type);
    }

    public InsurancePolicy renewPolicy(Long id, PolicyRenewRequest request) {
        InsurancePolicy policy = getPolicyById(id);

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BadRequestException("End date must be after start date");
        }

        InsurancePolicy renewed = new InsurancePolicy();
        renewed.setPolicyNumber(generatePolicyNumber());
        renewed.setPolicyType(policy.getPolicyType());
        renewed.setPremiumAmount(request.getPremiumAmount());
        renewed.setCoverageAmount(policy.getCoverageAmount());
        renewed.setStartDate(request.getStartDate());
        renewed.setEndDate(request.getEndDate());
        renewed.setCustomer(policy.getCustomer());
        renewed.setStatus(PolicyStatus.ACTIVE);
        return policyRepository.save(renewed);
    }

    public List<InsurancePolicy> searchPolicies(String keyword) {
        return policyRepository.search(keyword);
    }

    @Transactional
    public void markExpiredPolicies() {
        List<InsurancePolicy> expired = policyRepository.findExpiredPolicies(LocalDate.now());
        for (InsurancePolicy policy : expired) {
            policy.setStatus(PolicyStatus.EXPIRED);
        }
        policyRepository.saveAll(expired);
    }

    private String generatePolicyNumber() {
        return "POL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
