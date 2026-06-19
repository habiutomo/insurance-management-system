package com.insurance.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.insurance.enums.ClaimStatus;
import com.insurance.enums.PaymentStatus;
import com.insurance.enums.PolicyStatus;
import com.insurance.enums.PolicyType;
import com.insurance.repository.ClaimRepository;
import com.insurance.repository.CustomerRepository;
import com.insurance.repository.PaymentRepository;
import com.insurance.repository.PolicyRepository;

@Service
public class DashboardService {

    private final CustomerRepository customerRepository;
    private final PolicyRepository policyRepository;
    private final ClaimRepository claimRepository;
    private final PaymentRepository paymentRepository;

    public DashboardService(CustomerRepository customerRepository,
                            PolicyRepository policyRepository,
                            ClaimRepository claimRepository,
                            PaymentRepository paymentRepository) {
        this.customerRepository = customerRepository;
        this.policyRepository = policyRepository;
        this.claimRepository = claimRepository;
        this.paymentRepository = paymentRepository;
    }

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCustomers", customerRepository.count());
        stats.put("totalPolicies", policyRepository.count());
        stats.put("totalClaims", claimRepository.count());

        stats.put("activePolicies", policyRepository.countByStatus(PolicyStatus.ACTIVE));
        stats.put("expiredPolicies", policyRepository.countByStatus(PolicyStatus.EXPIRED));
        stats.put("cancelledPolicies", policyRepository.countByStatus(PolicyStatus.CANCELLED));
        stats.put("pendingPolicies", policyRepository.countByStatus(PolicyStatus.PENDING));

        stats.put("submittedClaims", claimRepository.countByStatus(ClaimStatus.SUBMITTED));
        stats.put("underReviewClaims", claimRepository.countByStatus(ClaimStatus.UNDER_REVIEW));
        stats.put("approvedClaims", claimRepository.countByStatus(ClaimStatus.APPROVED));
        stats.put("rejectedClaims", claimRepository.countByStatus(ClaimStatus.REJECTED));
        stats.put("paidClaims", claimRepository.countByStatus(ClaimStatus.PAID));

        stats.put("autoPolicies", policyRepository.countByPolicyType(PolicyType.AUTO));
        stats.put("healthPolicies", policyRepository.countByPolicyType(PolicyType.HEALTH));
        stats.put("lifePolicies", policyRepository.countByPolicyType(PolicyType.LIFE));
        stats.put("propertyPolicies", policyRepository.countByPolicyType(PolicyType.PROPERTY));
        stats.put("travelPolicies", policyRepository.countByPolicyType(PolicyType.TRAVEL));

        stats.put("paidPayments", paymentRepository.countByStatus(PaymentStatus.PAID));
        stats.put("pendingPayments", paymentRepository.countByStatus(PaymentStatus.PENDING));
        stats.put("overduePayments", paymentRepository.countByStatus(PaymentStatus.OVERDUE));

        return stats;
    }
}
