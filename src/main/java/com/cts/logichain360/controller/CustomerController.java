package com.cts.logichain360.controller;

import com.cts.logichain360.dto.request.UpdateCustomerRequest;
import com.cts.logichain360.dto.response.CustomerResponse;
import com.cts.logichain360.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CustomerResponse> getByUserId(@PathVariable Long userId) {
        return customerService.getCustomerByUserId(userId);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAll() {
        return customerService.getAllCustomers();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(@PathVariable Long id, @RequestBody UpdateCustomerRequest request) {
        return customerService.updateCustomer(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return customerService.deleteCustomer(id);
    }
}