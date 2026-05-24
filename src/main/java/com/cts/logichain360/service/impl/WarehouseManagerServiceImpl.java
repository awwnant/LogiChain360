package com.cts.logichain360.service.impl;

import com.cts.logichain360.dto.request.*;
import com.cts.logichain360.dto.response.WarehouseManagerResponse;
import com.cts.logichain360.entity.WarehouseManager;
import com.cts.logichain360.repository.WarehouseManagerRepository;
import com.cts.logichain360.service.WarehouseManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @RequiredArgsConstructor
public class WarehouseManagerServiceImpl implements WarehouseManagerService {
    private final WarehouseManagerRepository wmRepo;

    @Override
    public ResponseEntity<WarehouseManagerResponse> getById(Long id) {
        return wmRepo.findById(id)
                .map(wm -> ResponseEntity.ok(toResponse(wm)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<WarehouseManagerResponse> getByUserId(Long userId) {
        return wmRepo.findByUser_Id(userId)
                .map(wm -> ResponseEntity.ok(toResponse(wm)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<WarehouseManagerResponse>> getAll() {
        return ResponseEntity.ok(wmRepo.findAll().stream().map(this::toResponse).toList());
    }

    @Override
    @Transactional
    public ResponseEntity<WarehouseManagerResponse> update(Long id, UpdateWarehouseManagerRequest req) {
        return wmRepo.findById(id)
                .map(wm -> {
                    if (req.getEmployeeCode()          != null) wm.setEmployeeCode(req.getEmployeeCode());
                    if (req.getDesignation()           != null) wm.setDesignation(req.getDesignation());
                    if (req.getShift()                 != null) wm.setShift(req.getShift());
                    if (req.getAssignedWarehouseCode()  != null) wm.setAssignedWarehouseCode(req.getAssignedWarehouseCode());
                    return ResponseEntity.ok(toResponse(wmRepo.save(wm)));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @Transactional
    public ResponseEntity<WarehouseManagerResponse> assignWarehouse(Long id, WarehouseAssignmentRequest req) {
        return wmRepo.findById(id)
                .map(wm -> {
                    wm.setAssignedWarehouseCode(req.getWarehouseCode());
                    return ResponseEntity.ok(toResponse(wmRepo.save(wm)));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @Transactional
    public ResponseEntity<Void> delete(Long id) {
        return wmRepo.findById(id)
                .map(wm -> {
                    wmRepo.delete(wm);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private WarehouseManagerResponse toResponse(WarehouseManager wm) {
        return WarehouseManagerResponse.builder()
                .id(wm.getId()).userId(wm.getUser().getId())
                .userName(wm.getUser().getName()).userPhone(wm.getUser().getPhone())
                .employeeCode(wm.getEmployeeCode()).designation(wm.getDesignation())
                .shift(wm.getShift()).assignedWarehouseCode(wm.getAssignedWarehouseCode()).build();
    }
}