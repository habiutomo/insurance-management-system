package com.insurance.controller;

import com.insurance.dto.AgentRequest;
import com.insurance.dto.ApiResponse;
import com.insurance.entity.Agent;
import com.insurance.service.AgentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agents")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Agent>>> getAllAgents() {
        return ResponseEntity.ok(ApiResponse.ok(agentService.getAllAgents()));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<Agent>>> getActiveAgents() {
        return ResponseEntity.ok(ApiResponse.ok(agentService.getActiveAgents()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Agent>> getAgentById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(agentService.getAgentById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Agent>> createAgent(@Valid @RequestBody AgentRequest request) {
        Agent agent = agentService.createAgent(request);
        return ResponseEntity.ok(ApiResponse.ok("Agent created successfully", agent));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Agent>> updateAgent(
            @PathVariable Long id, @Valid @RequestBody AgentRequest request) {
        Agent agent = agentService.updateAgent(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Agent updated successfully", agent));
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<ApiResponse<Agent>> toggleAgentStatus(@PathVariable Long id) {
        agentService.toggleAgentStatus(id);
        Agent agent = agentService.getAgentById(id);
        return ResponseEntity.ok(ApiResponse.ok("Agent status toggled", agent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAgent(@PathVariable Long id) {
        agentService.deleteAgent(id);
        return ResponseEntity.ok(ApiResponse.ok("Agent deleted successfully", null));
    }
}
