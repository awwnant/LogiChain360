package com.cts.logichain360.controller;

import com.cts.logichain360.dto.request.*;
import com.cts.logichain360.dto.response.DriverResponse;
import com.cts.logichain360.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
public class DriverController {
    private final DriverService driverService;

    @GetMapping("/{id}")
    public ResponseEntity<DriverResponse> getById(@PathVariable Long id) {
        return driverService.getDriverById(id);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<DriverResponse> getByUserId(@PathVariable Long userId) {
        return driverService.getDriverByUserId(userId);
    }

    @GetMapping
    public ResponseEntity<List<DriverResponse>> getAll() {
        return driverService.getAllDrivers();
    }

    @GetMapping("/available")
    public ResponseEntity<List<DriverResponse>> getAvailable() {
        return driverService.getAvailableDrivers();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverResponse> update(@PathVariable Long id, @RequestBody UpdateDriverRequest request) {
        return driverService.updateDriver(id, request);
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<DriverResponse> updateAvailability(@PathVariable Long id,
                                                             @RequestBody DriverAvailabilityRequest request) {
        return driverService.updateAvailability(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return driverService.deleteDriver(id);
    }
}