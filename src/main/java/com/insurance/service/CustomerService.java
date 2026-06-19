package com.insurance.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.insurance.dto.CustomerRequest;
import com.insurance.entity.Customer;
import com.insurance.exception.BadRequestException;
import com.insurance.exception.ResourceNotFoundException;
import com.insurance.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    public Customer createCustomer(CustomerRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        if (request.getNik() != null && customerRepository.existsByNik(request.getNik())) {
            throw new BadRequestException("NIK already exists");
        }

        Customer customer = new Customer();
        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setPlaceOfBirth(request.getPlaceOfBirth());
        customer.setGender(request.getGender());
        customer.setReligion(request.getReligion());
        customer.setNik(request.getNik());
        customer.setNpwp(request.getNpwp());
        customer.setOccupation(request.getOccupation());
        customer.setMaritalStatus(request.getMaritalStatus());
        customer.setMotherMaidenName(request.getMotherMaidenName());
        customer.setNationality(request.getNationality());
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, CustomerRequest request) {
        Customer customer = getCustomerById(id);

        if (!customer.getEmail().equals(request.getEmail()) &&
            customerRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setPlaceOfBirth(request.getPlaceOfBirth());
        customer.setGender(request.getGender());
        customer.setReligion(request.getReligion());
        customer.setNik(request.getNik());
        customer.setNpwp(request.getNpwp());
        customer.setOccupation(request.getOccupation());
        customer.setMaritalStatus(request.getMaritalStatus());
        customer.setMotherMaidenName(request.getMotherMaidenName());
        customer.setNationality(request.getNationality());
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        customerRepository.delete(customer);
    }

    public List<Customer> searchCustomers(String keyword) {
        return customerRepository.search(keyword);
    }
}
