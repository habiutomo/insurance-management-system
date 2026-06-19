package com.insurance.config;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.insurance.entity.Agent;
import com.insurance.entity.Customer;
import com.insurance.entity.InsurancePolicy;
import com.insurance.entity.User;
import com.insurance.enums.Gender;
import com.insurance.enums.MaritalStatus;
import com.insurance.enums.PolicyStatus;
import com.insurance.enums.PolicyType;
import com.insurance.enums.PremiumFrequency;
import com.insurance.enums.PremiumPaymentMethod;
import com.insurance.enums.Religion;
import com.insurance.enums.Role;
import com.insurance.repository.AgentRepository;
import com.insurance.repository.CustomerRepository;
import com.insurance.repository.PolicyRepository;
import com.insurance.repository.UserRepository;
import com.insurance.service.PolicyService;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PolicyService policyService;
    private final CustomerRepository customerRepository;
    private final PolicyRepository policyRepository;
    private final AgentRepository agentRepository;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder,
                      PolicyService policyService, CustomerRepository customerRepository,
                      PolicyRepository policyRepository, AgentRepository agentRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.policyService = policyService;
        this.customerRepository = customerRepository;
        this.policyRepository = policyRepository;
        this.agentRepository = agentRepository;
    }

    @Override
    public void run(String... args) {
        seedUsers();
        seedAgents();
        seedCustomers();
        policyService.markExpiredPolicies();
    }

    private void seedUsers() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User(
                "admin",
                passwordEncoder.encode("admin123"),
                "admin@danamon.co.id",
                "Administrator Danamon",
                Role.ROLE_ADMIN
            );
            userRepository.save(admin);
        }

        if (!userRepository.existsByUsername("agent1")) {
            User agent = new User(
                "agent1",
                passwordEncoder.encode("agent123"),
                "agent1@danamon.co.id",
                "Budi Santoso",
                Role.ROLE_AGENT
            );
            userRepository.save(agent);
        }

        if (!userRepository.existsByUsername("customer1")) {
            User customer = new User(
                "customer1",
                passwordEncoder.encode("cust123"),
                "customer1@email.com",
                "Siti Rahayu",
                Role.ROLE_CUSTOMER
            );
            userRepository.save(customer);
        }
    }

    private void seedAgents() {
        if (agentRepository.count() == 0) {
            Agent a1 = new Agent();
            a1.setAgentCode("AGT-DMN-001");
            a1.setFullName("Budi Santoso");
            a1.setEmail("budi.santoso@danamon.co.id");
            a1.setPhone("081234567890");
            a1.setBranchOffice("Danamon KCU Thamrin");
            a1.setSupervisorName("Rina Wijaya");
            agentRepository.save(a1);

            Agent a2 = new Agent();
            a2.setAgentCode("AGT-DMN-002");
            a2.setFullName("Dewi Lestari");
            a2.setEmail("dewi.lestari@danamon.co.id");
            a2.setPhone("081298765432");
            a2.setBranchOffice("Danamon KCU Kelapa Gading");
            a2.setSupervisorName("Rina Wijaya");
            agentRepository.save(a2);

            Agent a3 = new Agent();
            a3.setAgentCode("AGT-DMN-003");
            a3.setFullName("Agus Prasetyo");
            a3.setEmail("agus.prasetyo@danamon.co.id");
            a3.setPhone("081234567891");
            a3.setBranchOffice("Danamon KCP Mangga Dua");
            a3.setSupervisorName("Hendra Gunawan");
            agentRepository.save(a3);
        }
    }

    private void seedCustomers() {
        if (customerRepository.count() == 0) {
            Customer c1 = new Customer();
            c1.setFullName("Siti Rahayu");
            c1.setEmail("siti.rahayu@email.com");
            c1.setPhone("081111111111");
            c1.setAddress("Jl. Merdeka No. 10, Jakarta Pusat");
            c1.setDateOfBirth(LocalDate.of(1990, 5, 15));
            c1.setPlaceOfBirth("Jakarta");
            c1.setGender(Gender.PEREMPUAN);
            c1.setReligion(Religion.ISLAM);
            c1.setNik("3174015205900001");
            c1.setNpwp("12.345.678.9-012.000");
            c1.setOccupation("Pegawai Swasta");
            c1.setMaritalStatus(MaritalStatus.MENIKAH);
            c1.setMotherMaidenName("Sumarni");
            c1.setNationality("WNI");
            customerRepository.save(c1);

            Customer c2 = new Customer();
            c2.setFullName("Hendra Gunawan");
            c2.setEmail("hendra.gunawan@email.com");
            c2.setPhone("082222222222");
            c2.setAddress("Jl. Sudirman No. 25, Jakarta Selatan");
            c2.setDateOfBirth(LocalDate.of(1985, 3, 20));
            c2.setPlaceOfBirth("Bandung");
            c2.setGender(Gender.LAKI_LAKI);
            c2.setReligion(Religion.KRISTEN);
            c2.setNik("3274012003850002");
            c2.setNpwp("98.765.432.1-012.000");
            c2.setOccupation("Pengusaha");
            c2.setMaritalStatus(MaritalStatus.MENIKAH);
            c2.setMotherMaidenName("Yuliana");
            c2.setNationality("WNI");
            customerRepository.save(c2);

            Customer c3 = new Customer();
            c3.setFullName("Rina Wijaya");
            c3.setEmail("rina.wijaya@email.com");
            c3.setPhone("083333333333");
            c3.setAddress("Jl. Kemang Raya No. 5, Jakarta Selatan");
            c3.setDateOfBirth(LocalDate.of(1995, 11, 8));
            c3.setPlaceOfBirth("Surabaya");
            c3.setGender(Gender.PEREMPUAN);
            c3.setReligion(Religion.ISLAM);
            c3.setNik("3578010811950003");
            c3.setOccupation("Dokter");
            c3.setMaritalStatus(MaritalStatus.BELUM_MENIKAH);
            c3.setMotherMaidenName("Kartini");
            c3.setNationality("WNI");
            customerRepository.save(c3);

            seedPolicies(c1, c2, c3);
        }
    }

    private void seedPolicies(Customer c1, Customer c2, Customer c3) {
        if (policyRepository.count() == 0) {
            InsurancePolicy p1 = new InsurancePolicy();
            p1.setPolicyNumber("POL-DMN-2024-0001");
            p1.setPolicyType(PolicyType.HEALTH);
            p1.setStatus(PolicyStatus.ACTIVE);
            p1.setPremiumAmount(new BigDecimal("500000"));
            p1.setCoverageAmount(new BigDecimal("50000000"));
            p1.setStartDate(LocalDate.now());
            p1.setEndDate(LocalDate.now().plusYears(1));
            p1.setCustomer(c1);
            p1.setInsuredName("Siti Rahayu");
            p1.setBeneficiaryName("Teguh Santoso");
            p1.setBeneficiaryRelationship("Suami");
            p1.setBankAccountNumber("1234567890");
            p1.setBankAccountName("Siti Rahayu");
            p1.setBankName("Bank Danamon");
            p1.setPremiumPaymentMethod(PremiumPaymentMethod.AUTO_DEBIT);
            p1.setPremiumFrequency(PremiumFrequency.BULANAN);
            p1.setBranchOffice("Danamon KCU Thamrin");
            p1.setAutoRenew(true);
            policyRepository.save(p1);

            InsurancePolicy p2 = new InsurancePolicy();
            p2.setPolicyNumber("POL-DMN-2024-0002");
            p2.setPolicyType(PolicyType.LIFE);
            p2.setStatus(PolicyStatus.ACTIVE);
            p2.setPremiumAmount(new BigDecimal("1000000"));
            p2.setCoverageAmount(new BigDecimal("200000000"));
            p2.setStartDate(LocalDate.now());
            p2.setEndDate(LocalDate.now().plusYears(5));
            p2.setCustomer(c2);
            p2.setInsuredName("Hendra Gunawan");
            p2.setBeneficiaryName("Dewi Gunawan");
            p2.setBeneficiaryRelationship("Istri");
            p2.setBankAccountNumber("0987654321");
            p2.setBankAccountName("Hendra Gunawan");
            p2.setBankName("Bank Danamon");
            p2.setPremiumPaymentMethod(PremiumPaymentMethod.AUTO_DEBIT);
            p2.setPremiumFrequency(PremiumFrequency.TAHUNAN);
            p2.setBranchOffice("Danamon KCU Kelapa Gading");
            p2.setAutoRenew(true);
            policyRepository.save(p2);

            InsurancePolicy p3 = new InsurancePolicy();
            p3.setPolicyNumber("POL-DMN-2024-0003");
            p3.setPolicyType(PolicyType.AUTO);
            p3.setStatus(PolicyStatus.ACTIVE);
            p3.setPremiumAmount(new BigDecimal("2500000"));
            p3.setCoverageAmount(new BigDecimal("300000000"));
            p3.setStartDate(LocalDate.now());
            p3.setEndDate(LocalDate.now().plusYears(1));
            p3.setCustomer(c3);
            p3.setInsuredName("Rina Wijaya");
            p3.setBeneficiaryName("Orang Tua");
            p3.setBeneficiaryRelationship("Ayah/Ibu");
            p3.setBankAccountNumber("1122334455");
            p3.setBankAccountName("Rina Wijaya");
            p3.setBankName("Bank Danamon");
            p3.setPremiumPaymentMethod(PremiumPaymentMethod.VIRTUAL_ACCOUNT);
            p3.setPremiumFrequency(PremiumFrequency.SEKALIGUS);
            p3.setBranchOffice("Danamon KCP Mangga Dua");
            p3.setAutoRenew(false);
            policyRepository.save(p3);
        }
    }
}
