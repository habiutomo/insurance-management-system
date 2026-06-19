package com.insurance.service;

import com.insurance.dto.PaymentRequest;
import com.insurance.entity.InsurancePolicy;
import com.insurance.entity.Payment;
import com.insurance.enums.PaymentStatus;
import com.insurance.exception.BadRequestException;
import com.insurance.exception.ResourceNotFoundException;
import com.insurance.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PolicyService policyService;

    public PaymentService(PaymentRepository paymentRepository, PolicyService policyService) {
        this.paymentRepository = paymentRepository;
        this.policyService = policyService;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
    }

    public Payment createPayment(PaymentRequest request) {
        InsurancePolicy policy = policyService.getPolicyById(request.getPolicyId());

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Payment amount must be positive");
        }

        Payment payment = new Payment();
        payment.setPaymentNumber(generatePaymentNumber());
        payment.setAmount(request.getAmount());
        payment.setPaymentDate(request.getPaymentDate());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPolicy(policy);
        payment.setStatus(PaymentStatus.PAID);
        return paymentRepository.save(payment);
    }

    public Payment updatePaymentStatus(Long id, PaymentStatus status) {
        Payment payment = getPaymentById(id);
        payment.setStatus(status);
        return paymentRepository.save(payment);
    }

    public List<Payment> getPaymentsByPolicy(Long policyId) {
        return paymentRepository.findByPolicyId(policyId);
    }

    public void deletePayment(Long id) {
        Payment payment = getPaymentById(id);
        paymentRepository.delete(payment);
    }

    private String generatePaymentNumber() {
        return "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
