package com.cts.logichain360.service.impl;

import com.cts.logichain360.dto.request.UpdateVendorRequest;
import com.cts.logichain360.dto.response.VendorResponse;
import com.cts.logichain360.entity.Vendor;
import com.cts.logichain360.repository.VendorRepository;
import com.cts.logichain360.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {
    private final VendorRepository vendorRepo;

    @Override
    public ResponseEntity<VendorResponse> getVendorById(Long id) {
        return vendorRepo.findById(id)
                .map(vendor -> ResponseEntity.ok(toResponse(vendor)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<VendorResponse> getVendorByUserId(Long userId) {
        return vendorRepo.findByUser_Id(userId)
                .map(vendor -> ResponseEntity.ok(toResponse(vendor)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<VendorResponse>> getAllVendors() {
        return ResponseEntity.ok(vendorRepo.findAll().stream().map(this::toResponse).toList());
    }

    @Override
    @Transactional
    public ResponseEntity<VendorResponse> updateVendor(Long id, UpdateVendorRequest req) {
        return vendorRepo.findById(id)
                .map(v -> {
                    if (req.getCompanyName()     != null) v.setCompanyName(req.getCompanyName());
                    if (req.getGstNumber()       != null) v.setGstNumber(req.getGstNumber());
                    if (req.getEmail()           != null) v.setEmail(req.getEmail());
                    if (req.getBusinessAddress() != null) v.setBusinessAddress(req.getBusinessAddress());
                    if (req.getContactPerson()   != null) v.setContactPerson(req.getContactPerson());
                    if (req.getPaymentTerms()    != null) v.setPaymentTerms(req.getPaymentTerms());
                    return ResponseEntity.ok(toResponse(vendorRepo.save(v)));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteVendor(Long id) {
        return vendorRepo.findById(id)
                .map(v -> {
                    vendorRepo.delete(v);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private VendorResponse toResponse(Vendor v) {
        return VendorResponse.builder()
                .id(v.getId()).userId(v.getUser().getId())
                .userName(v.getUser().getName()).userPhone(v.getUser().getPhone())
                .companyName(v.getCompanyName()).gstNumber(v.getGstNumber())
                .email(v.getEmail()).businessAddress(v.getBusinessAddress())
                .contactPerson(v.getContactPerson()).paymentTerms(v.getPaymentTerms()).build();
    }
}