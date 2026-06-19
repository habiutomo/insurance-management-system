package com.insurance.dto;

import com.insurance.entity.Customer;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CustomerResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
    private String idCardNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CustomerResponse from(Customer customer) {
        CustomerResponse r = new CustomerResponse();
        r.setId(customer.getId());
        r.setFullName(customer.getFullName());
        r.setEmail(customer.getEmail());
        r.setPhone(customer.getPhone());
        r.setAddress(customer.getAddress());
        r.setDateOfBirth(customer.getDateOfBirth());
        r.setIdCardNumber(customer.getIdCardNumber());
        r.setCreatedAt(customer.getCreatedAt());
        r.setUpdatedAt(customer.getUpdatedAt());
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getIdCardNumber() { return idCardNumber; }
    public void setIdCardNumber(String idCardNumber) { this.idCardNumber = idCardNumber; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
