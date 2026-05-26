package com.cts.logichain360.service.impl;

import com.cts.logichain360.dto.request.*;
import com.cts.logichain360.dto.response.WarehouseManagerResponse;
import com.cts.logichain360.entity.Warehouse;
import com.cts.logichain360.entity.WarehouseManager;
import com.cts.logichain360.exception.ResourceNotFoundException;
import com.cts.logichain360.exception.UserAlreadyExistsException;
import com.cts.logichain360.repository.WarehouseManagerRepository;
import com.cts.logichain360.repository.WarehouseRepository;
import com.cts.logichain360.service.WarehouseManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @RequiredArgsConstructor
public class WarehouseManagerServiceImpl implements WarehouseManagerService {

    private final WarehouseManagerRepository wmRepo;
    private final WarehouseRepository        warehouseRepo;

    @Override
    public ResponseEntity<WarehouseManagerResponse> getById(Long id) {
        WarehouseManager wm = wmRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Warehouse manager " + id + " not found."));
        return ResponseEntity.ok(toResponse(wm));
    }

    @Override
    public ResponseEntity<WarehouseManagerResponse> getByUserId(Long userId) {
        WarehouseManager wm = wmRepo.findByUser_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No warehouse manager found for user " + userId + "."));
        return ResponseEntity.ok(toResponse(wm));
    }

    @Override
    public ResponseEntity<List<WarehouseManagerResponse>> getAll() {
        return ResponseEntity.ok(wmRepo.findAll().stream().map(this::toResponse).toList());
    }

    @Override
    @Transactional
    public ResponseEntity<WarehouseManagerResponse> update(Long id, UpdateWarehouseManagerRequest req) {
        WarehouseManager wm = wmRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Warehouse manager " + id + " not found."));

        if (req.getEmployeeCode() != null) wm.setEmployeeCode(req.getEmployeeCode());
        if (req.getDesignation()  != null) wm.setDesignation(req.getDesignation());
        if (req.getShift()        != null) wm.setShift(req.getShift());

        return ResponseEntity.ok(toResponse(wmRepo.save(wm)));
    }

    @Override
    @Transactional
    public ResponseEntity<WarehouseManagerResponse> assignWarehouse(Long id, WarehouseAssignmentRequest req) {
        WarehouseManager wm = wmRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Warehouse manager " + id + " not found."));

        // null warehouseId → un-assign
        if (req.getWarehouseId() == null) {
            wm.setAssignedWarehouse(null);
            return ResponseEntity.ok(toResponse(wmRepo.save(wm)));
        }

        Warehouse target = warehouseRepo.findById(req.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Warehouse " + req.getWarehouseId() + " not found."));

        // Enforce 1-to-1: refuse if some other manager already holds this warehouse
        boolean alreadyTaken = wmRepo.findAll().stream()
                .anyMatch(other -> !other.getId().equals(wm.getId())
                                && other.getAssignedWarehouse() != null
                                && other.getAssignedWarehouse().getId().equals(target.getId()));
        if (alreadyTaken) {
            throw new UserAlreadyExistsException(
                    "Warehouse " + target.getId() + " is already assigned to another manager.");
        }

        wm.setAssignedWarehouse(target);
        return ResponseEntity.ok(toResponse(wmRepo.save(wm)));
    }

    @Override
    @Transactional
    public ResponseEntity<Void> delete(Long id) {
        WarehouseManager wm = wmRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Warehouse manager " + id + " not found."));
        wmRepo.delete(wm);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private WarehouseManagerResponse toResponse(WarehouseManager wm) {
        Warehouse aw = wm.getAssignedWarehouse();
        return WarehouseManagerResponse.builder()
                .id(wm.getId())
                .userId(wm.getUser().getId())
                .userName(wm.getUser().getName())
                .userPhone(wm.getUser().getPhone())
                .employeeCode(wm.getEmployeeCode())
                .designation(wm.getDesignation())
                .shift(wm.getShift())
                .assignedWarehouseId(aw == null ? null : aw.getId())
                .assignedWarehouseCode(aw == null ? null : aw.getWarehouseCode())
                .assignedWarehouseLocation(aw == null ? null : aw.getLocation())
                .build();
    }
}