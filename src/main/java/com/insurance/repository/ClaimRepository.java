package com.insurance.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.insurance.entity.Claim;
import com.insurance.enums.ClaimStatus;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
    Optional<Claim> findByClaimNumber(String claimNumber);
    List<Claim> findByPolicyId(Long policyId);
    List<Claim> findByStatus(ClaimStatus status);
    long countByStatus(ClaimStatus status);
    long countByStatusAndPolicyId(ClaimStatus status, Long policyId);

    List<Claim> findByIncidentDateBetween(LocalDate start, LocalDate end);

    @Query("SELECT c FROM Claim c WHERE " +
           "LOWER(c.claimNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Claim> search(@Param("keyword") String keyword);
}
