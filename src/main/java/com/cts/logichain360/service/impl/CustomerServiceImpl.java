package com.cts.logichain360.service.impl;

import com.cts.logichain360.dto.request.UpdateCustomerRequest;
import com.cts.logichain360.dto.response.CustomerResponse;
import com.cts.logichain360.entity.Customer;
import com.cts.logichain360.repository.CustomerRepository;
import com.cts.logichain360.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepo;

    @Override
    public ResponseEntity<CustomerResponse> getCustomerById(Long id) {
        return customerRepo.findById(id)
                .map(c -> ResponseEntity.ok(toResponse(c)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<CustomerResponse> getCustomerByUserId(Long userId) {
        return customerRepo.findByUser_Id(userId)
                .map(c -> ResponseEntity.ok(toResponse(c)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(customerRepo.findAll().stream().map(this::toResponse).toList());
    }

    @Override
    @Transactional
    public ResponseEntity<CustomerResponse> updateCustomer(Long id, UpdateCustomerRequest req) {
        return customerRepo.findById(id)
                .map(c -> {
                    if (req.getCompanyName()     != null) c.setCompanyName(req.getCompanyName());
                    if (req.getGstNumber()       != null) c.setGstNumber(req.getGstNumber());
                    if (req.getEmail()           != null) c.setEmail(req.getEmail());
                    if (req.getShippingAddress() != null) c.setShippingAddress(req.getShippingAddress());
                    if (req.getBillingAddress()  != null) c.setBillingAddress(req.getBillingAddress());
                    if (req.getCreditLimit()     != null) c.setCreditLimit(req.getCreditLimit());
                    if (req.getPaymentTerms()    != null) c.setPaymentTerms(req.getPaymentTerms());
                    return ResponseEntity.ok(toResponse(customerRepo.save(c)));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteCustomer(Long id) {
        return customerRepo.findById(id)
                .map(c -> {
                    customerRepo.delete(c);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private CustomerResponse toResponse(Customer c) {
        return CustomerResponse.builder()
                .id(c.getId()).userId(c.getUser().getId())
                .userName(c.getUser().getName()).userPhone(c.getUser().getPhone())
                .companyName(c.getCompanyName()).gstNumber(c.getGstNumber())
                .email(c.getEmail()).shippingAddress(c.getShippingAddress())
                .billingAddress(c.getBillingAddress())
                .creditLimit(c.getCreditLimit()).paymentTerms(c.getPaymentTerms()).build();
    }
}