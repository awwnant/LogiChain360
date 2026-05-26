package com.cts.logichain360.controller;

import com.cts.logichain360.dto.request.UpdateVendorRequest;
import com.cts.logichain360.dto.response.VendorResponse;
import com.cts.logichain360.service.VendorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/vendors")
@RequiredArgsConstructor
@Tag(name = "Vendor Management", description = "APIs for managing vendors")
public class VendorController {
    private final VendorService vendorService;

    @Operation(summary = "Get vendor by ID", description = "Retrieves a specific vendor by their unique ID")
    @GetMapping("/{id}")
    public ResponseEntity<VendorResponse> getById(@PathVariable Long id) {
        return vendorService.getVendorById(id);
    }

    @Operation(summary = "Get vendor by User ID", description = "Retrieves a specific vendor by their associated user ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<VendorResponse> getByUserId(@PathVariable Long userId) {
        return vendorService.getVendorByUserId(userId);
    }

    @Operation(summary = "Get all vendors", description = "Retrieves a list of all vendors")
    @GetMapping
    public ResponseEntity<List<VendorResponse>> getAll() {
        return vendorService.getAllVendors();
    }

    @Operation(summary = "Update a vendor", description = "Updates an existing vendor's details by their ID")
    @PutMapping("/{id}")
    public ResponseEntity<VendorResponse> update(@PathVariable Long id, @RequestBody UpdateVendorRequest request) {
        return vendorService.updateVendor(id, request);
    }

    @Operation(summary = "Delete a vendor", description = "Deletes a vendor by their ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return vendorService.deleteVendor(id);
    }
}