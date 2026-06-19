package com.insurance.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.insurance.entity.InsurancePolicy;
import com.insurance.enums.PolicyStatus;
import com.insurance.enums.PolicyType;
import com.insurance.enums.UnderwritingStatus;

public interface PolicyRepository extends JpaRepository<InsurancePolicy, Long> {
    Optional<InsurancePolicy> findByPolicyNumber(String policyNumber);
    List<InsurancePolicy> findByCustomerId(Long customerId);
    List<InsurancePolicy> findByPolicyType(PolicyType policyType);
    List<InsurancePolicy> findByStatus(PolicyStatus status);
    boolean existsByPolicyNumber(String policyNumber);
    long countByStatus(PolicyStatus status);
    long countByPolicyType(PolicyType policyType);
    long countByUnderwritingStatus(UnderwritingStatus underwritingStatus);

    List<InsurancePolicy> findByStartDateBetween(LocalDate start, LocalDate end);
    List<InsurancePolicy> findByEndDateBetween(LocalDate start, LocalDate end);

    @Query("SELECT p FROM InsurancePolicy p WHERE p.endDate < :now AND p.status <> 'EXPIRED'")
    List<InsurancePolicy> findExpiredPolicies(@Param("now") LocalDate now);

    @Query("SELECT p FROM InsurancePolicy p WHERE " +
           "LOWER(p.policyNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<InsurancePolicy> search(@Param("keyword") String keyword);
}
