package com.insurance.repository;

import com.insurance.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgentRepository extends JpaRepository<Agent, Long> {
    Optional<Agent> findByAgentCode(String agentCode);
    List<Agent> findByBranchOffice(String branchOffice);
    List<Agent> findByActiveTrue();
    boolean existsByAgentCode(String agentCode);
}
