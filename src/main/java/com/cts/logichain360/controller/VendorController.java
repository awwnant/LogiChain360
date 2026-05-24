package com.cts.logichain360.controller;

import com.cts.logichain360.dto.request.UpdateVendorRequest;
import com.cts.logichain360.dto.response.VendorResponse;
import com.cts.logichain360.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/vendors")
@RequiredArgsConstructor
public class VendorController {
    private final VendorService vendorService;

    @GetMapping("/{id}")
    public ResponseEntity<VendorResponse> getById(@PathVariable Long id) {
        return vendorService.getVendorById(id);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<VendorResponse> getByUserId(@PathVariable Long userId) {
        return vendorService.getVendorByUserId(userId);
    }

    @GetMapping
    public ResponseEntity<List<VendorResponse>> getAll() {
        return vendorService.getAllVendors();
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendorResponse> update(@PathVariable Long id, @RequestBody UpdateVendorRequest request) {
        return vendorService.updateVendor(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return vendorService.deleteVendor(id);
    }
}