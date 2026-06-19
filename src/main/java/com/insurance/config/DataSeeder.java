package com.insurance.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.insurance.entity.User;
import com.insurance.enums.Role;
import com.insurance.repository.UserRepository;
import com.insurance.service.PolicyService;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PolicyService policyService;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder,
                      PolicyService policyService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.policyService = policyService;
    }

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User(
                "admin",
                passwordEncoder.encode("admin123"),
                "admin@insurance.com",
                "System Admin",
                Role.ROLE_ADMIN
            );
            userRepository.save(admin);
        }

        if (!userRepository.existsByUsername("agent1")) {
            User agent = new User(
                "agent1",
                passwordEncoder.encode("agent123"),
                "agent1@insurance.com",
                "Agent One",
                Role.ROLE_AGENT
            );
            userRepository.save(agent);
        }

        if (!userRepository.existsByUsername("customer1")) {
            User customer = new User(
                "customer1",
                passwordEncoder.encode("cust123"),
                "customer1@insurance.com",
                "Customer One",
                Role.ROLE_CUSTOMER
            );
            userRepository.save(customer);
        }

        policyService.markExpiredPolicies();
    }
}
