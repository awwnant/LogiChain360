package com.cts.logichain360.service.impl;

import com.cts.logichain360.dto.request.*;
import com.cts.logichain360.dto.response.DriverResponse;
import com.cts.logichain360.entity.Driver;
import com.cts.logichain360.repository.DriverRepository;
import com.cts.logichain360.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
    private final DriverRepository driverRepo;

    @Override
    public ResponseEntity<DriverResponse> getDriverById(Long id) {
        return driverRepo.findById(id)
                .map(d -> ResponseEntity.ok(toResponse(d)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<DriverResponse> getDriverByUserId(Long userId) {
        return driverRepo.findByUser_Id(userId)
                .map(d -> ResponseEntity.ok(toResponse(d)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<DriverResponse>> getAllDrivers() {
        return ResponseEntity.ok(driverRepo.findAll().stream().map(this::toResponse).toList());
    }

    @Override
    public ResponseEntity<List<DriverResponse>> getAvailableDrivers() {
        return ResponseEntity.ok(driverRepo.findAllByAvailableTrue().stream().map(this::toResponse).toList());
    }

    @Override
    @Transactional
    public ResponseEntity<DriverResponse> updateDriver(Long id, UpdateDriverRequest req) {
        return driverRepo.findById(id)
                .map(d -> {
                    if (req.getLicenseNumber()     != null) d.setLicenseNumber(req.getLicenseNumber());
                    if (req.getLicenseExpiry()     != null) d.setLicenseExpiry(req.getLicenseExpiry());
                    if (req.getVehiclePreference() != null) d.setVehiclePreference(req.getVehiclePreference());
                    return ResponseEntity.ok(toResponse(driverRepo.save(d)));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @Transactional
    public ResponseEntity<DriverResponse> updateAvailability(Long id, DriverAvailabilityRequest req) {
        return driverRepo.findById(id)
                .map(d -> {
                    d.setAvailable(req.getAvailable());
                    return ResponseEntity.ok(toResponse(driverRepo.save(d)));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteDriver(Long id) {
        return driverRepo.findById(id)
                .map(d -> {
                    driverRepo.delete(d);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private DriverResponse toResponse(Driver d) {
        return DriverResponse.builder()
                .id(d.getId()).userId(d.getUser().getId())
                .userName(d.getUser().getName()).userPhone(d.getUser().getPhone())
                .licenseNumber(d.getLicenseNumber()).licenseExpiry(d.getLicenseExpiry())
                .vehiclePreference(d.getVehiclePreference()).available(d.getAvailable()).build();
    }
}