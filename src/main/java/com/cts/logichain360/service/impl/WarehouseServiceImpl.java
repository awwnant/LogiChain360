package com.cts.logichain360.service.impl;

import com.cts.logichain360.dto.request.*;
import com.cts.logichain360.dto.response.WarehouseResponse;
import com.cts.logichain360.entity.Warehouse;
import com.cts.logichain360.entity.WarehouseManager;
import com.cts.logichain360.exception.ResourceNotFoundException;
import com.cts.logichain360.exception.UserAlreadyExistsException;
import com.cts.logichain360.repository.WarehouseManagerRepository;
import com.cts.logichain360.repository.WarehouseRepository;
import com.cts.logichain360.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository        warehouseRepo;
    private final WarehouseManagerRepository wmRepo;

    @Override
    @Transactional
    public ResponseEntity<WarehouseResponse> createWarehouse(CreateWarehouseRequest req) {
        if (warehouseRepo.existsByWarehouseCode(req.getWarehouseCode())) {
            throw new UserAlreadyExistsException(
                    "Warehouse code '" + req.getWarehouseCode() + "' already in use.");
        }
        Warehouse saved = warehouseRepo.save(Warehouse.builder()
                .warehouseCode(req.getWarehouseCode())
                .location(req.getLocation())
                .capacity(req.getCapacity())
                .build());
        return new ResponseEntity<>(toResponse(saved), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<WarehouseResponse> getById(Long id) {
        Warehouse w = warehouseRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse " + id + " not found."));
        return ResponseEntity.ok(toResponse(w));
    }

    @Override
    public ResponseEntity<List<WarehouseResponse>> getAll() {
        return ResponseEntity.ok(warehouseRepo.findAll().stream().map(this::toResponse).toList());
    }

    @Override
    @Transactional
    public ResponseEntity<WarehouseResponse> update(Long id, UpdateWarehouseRequest req) {
        Warehouse w = warehouseRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse " + id + " not found."));

        if (req.getWarehouseCode() != null && !req.getWarehouseCode().equals(w.getWarehouseCode())) {
            if (warehouseRepo.existsByWarehouseCode(req.getWarehouseCode())) {
                throw new UserAlreadyExistsException(
                        "Warehouse code '" + req.getWarehouseCode() + "' already in use.");
            }
            w.setWarehouseCode(req.getWarehouseCode());
        }
        if (req.getLocation() != null) w.setLocation(req.getLocation());
        if (req.getCapacity() != null) w.setCapacity(req.getCapacity());

        return ResponseEntity.ok(toResponse(warehouseRepo.save(w)));
    }

    @Override
    @Transactional
    public ResponseEntity<Void> delete(Long id) {
        Warehouse w = warehouseRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse " + id + " not found."));

        // If a manager points at this warehouse, clear that link first
        // (otherwise the FK constraint would fail on hard delete)
        wmRepo.findAll().stream()
                .filter(m -> m.getAssignedWarehouse() != null
                          && m.getAssignedWarehouse().getId().equals(id))
                .forEach(m -> {
                    m.setAssignedWarehouse(null);
                    wmRepo.save(m);
                });

        warehouseRepo.delete(w);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private WarehouseResponse toResponse(Warehouse w) {
        // Manager is on the other side of the relationship — look it up to flatten into the DTO.
        // For tomorrow's demo this is fine; in production add a findByAssignedWarehouse_Id
        // query on WarehouseManagerRepository to avoid scanning all managers.
        WarehouseManager manager = wmRepo.findAll().stream()
                .filter(m -> m.getAssignedWarehouse() != null
                          && m.getAssignedWarehouse().getId().equals(w.getId()))
                .findFirst()
                .orElse(null);

        return WarehouseResponse.builder()
                .id(w.getId())
                .warehouseCode(w.getWarehouseCode())
                .location(w.getLocation())
                .capacity(w.getCapacity())
                .managerId(manager == null ? null : manager.getId())
                .managerName(manager == null ? null : manager.getUser().getName())
                .managerEmployeeCode(manager == null ? null : manager.getEmployeeCode())
                .build();
    }
}