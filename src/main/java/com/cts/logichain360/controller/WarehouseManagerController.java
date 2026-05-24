package com.cts.logichain360.controller;

import com.cts.logichain360.dto.request.*;
import com.cts.logichain360.dto.response.WarehouseManagerResponse;
import com.cts.logichain360.service.WarehouseManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/warehouse-managers")
@RequiredArgsConstructor
public class WarehouseManagerController {
    private final WarehouseManagerService wmService;

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseManagerResponse> getById(@PathVariable Long id) {
        return wmService.getById(id);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<WarehouseManagerResponse> getByUserId(@PathVariable Long userId) {
        return wmService.getByUserId(userId);
    }

    @GetMapping
    public ResponseEntity<List<WarehouseManagerResponse>> getAll() {
        return wmService.getAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseManagerResponse> update(@PathVariable Long id,
                                                           @RequestBody UpdateWarehouseManagerRequest request) {
        return wmService.update(id, request);
    }

    @PatchMapping("/{id}/assign-warehouse")
    public ResponseEntity<WarehouseManagerResponse> assignWarehouse(@PathVariable Long id,
                                                                    @RequestBody WarehouseAssignmentRequest request) {
        return wmService.assignWarehouse(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return wmService.delete(id);
    }
}