package com.insurance.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insurance.dto.ApiResponse;
import com.insurance.dto.PaymentRequest;
import com.insurance.entity.Payment;
import com.insurance.enums.PaymentStatus;
import com.insurance.service.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Payment>>> getAllPayments() {
        return ResponseEntity.ok(ApiResponse.ok(paymentService.getAllPayments()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Payment>> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(paymentService.getPaymentById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Payment>> createPayment(@Valid @RequestBody PaymentRequest request) {
        Payment payment = paymentService.createPayment(request);
        return ResponseEntity.ok(ApiResponse.ok("Payment created successfully", payment));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Payment>> updatePaymentStatus(
            @PathVariable Long id, @RequestParam PaymentStatus status) {
        Payment payment = paymentService.updatePaymentStatus(id, status);
        return ResponseEntity.ok(ApiResponse.ok("Payment status updated", payment));
    }

    @GetMapping("/policy/{policyId}")
    public ResponseEntity<ApiResponse<List<Payment>>> getPaymentsByPolicy(@PathVariable Long policyId) {
        return ResponseEntity.ok(ApiResponse.ok(paymentService.getPaymentsByPolicy(policyId)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.ok(ApiResponse.ok("Payment deleted successfully", null));
    }
}
