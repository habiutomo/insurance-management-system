package com.insurance.repository;

import com.insurance.entity.Commission;
import com.insurance.enums.CommissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommissionRepository extends JpaRepository<Commission, Long> {
    List<Commission> findByAgentId(Long agentId);
    List<Commission> findByPolicyId(Long policyId);
    List<Commission> findByStatus(CommissionStatus status);
    List<Commission> findByBranchOffice(String branchOffice);
    long countByStatus(CommissionStatus status);
}
