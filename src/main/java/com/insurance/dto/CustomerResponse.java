package com.insurance.dto;

import com.insurance.entity.Customer;
import com.insurance.enums.Gender;
import com.insurance.enums.MaritalStatus;
import com.insurance.enums.Religion;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CustomerResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
    private String placeOfBirth;
    private Gender gender;
    private Religion religion;
    private String nik;
    private String npwp;
    private String occupation;
    private MaritalStatus maritalStatus;
    private String motherMaidenName;
    private String nationality;
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
        r.setPlaceOfBirth(customer.getPlaceOfBirth());
        r.setGender(customer.getGender());
        r.setReligion(customer.getReligion());
        r.setNik(customer.getNik());
        r.setNpwp(customer.getNpwp());
        r.setOccupation(customer.getOccupation());
        r.setMaritalStatus(customer.getMaritalStatus());
        r.setMotherMaidenName(customer.getMotherMaidenName());
        r.setNationality(customer.getNationality());
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
    public String getPlaceOfBirth() { return placeOfBirth; }
    public void setPlaceOfBirth(String placeOfBirth) { this.placeOfBirth = placeOfBirth; }
    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }
    public Religion getReligion() { return religion; }
    public void setReligion(Religion religion) { this.religion = religion; }
    public String getNik() { return nik; }
    public void setNik(String nik) { this.nik = nik; }
    public String getNpwp() { return npwp; }
    public void setNpwp(String npwp) { this.npwp = npwp; }
    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    public MaritalStatus getMaritalStatus() { return maritalStatus; }
    public void setMaritalStatus(MaritalStatus maritalStatus) { this.maritalStatus = maritalStatus; }
    public String getMotherMaidenName() { return motherMaidenName; }
    public void setMotherMaidenName(String motherMaidenName) { this.motherMaidenName = motherMaidenName; }
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
