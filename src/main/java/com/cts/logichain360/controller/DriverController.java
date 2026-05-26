package com.cts.logichain360.controller;

import com.cts.logichain360.dto.request.*;
import com.cts.logichain360.dto.response.DriverResponse;
import com.cts.logichain360.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
@Tag(name = "Driver Management", description = "APIs for managing drivers")
public class DriverController {
    private final DriverService driverService;

    @Operation(summary = "Get driver by ID", description = "Retrieves a specific driver by their unique ID")
    @GetMapping("/{id}")
    public ResponseEntity<DriverResponse> getById(@PathVariable Long id) {
        return driverService.getDriverById(id);
    }

    @Operation(summary = "Get driver by User ID", description = "Retrieves a specific driver by their associated user ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<DriverResponse> getByUserId(@PathVariable Long userId) {
        return driverService.getDriverByUserId(userId);
    }

    @Operation(summary = "Get all drivers", description = "Retrieves a list of all drivers")
    @GetMapping
    public ResponseEntity<List<DriverResponse>> getAll() {
        return driverService.getAllDrivers();
    }

    @Operation(summary = "Get available drivers", description = "Retrieves a list of all currently available drivers")
    @GetMapping("/available")
    public ResponseEntity<List<DriverResponse>> getAvailable() {
        return driverService.getAvailableDrivers();
    }

    @Operation(summary = "Update a driver", description = "Updates an existing driver's details by their ID")
    @PutMapping("/{id}")
    public ResponseEntity<DriverResponse> update(@PathVariable Long id, @RequestBody UpdateDriverRequest request) {
        return driverService.updateDriver(id, request);
    }

    @Operation(summary = "Update driver availability", description = "Updates the availability status of a specific driver")
    @PatchMapping("/{id}/availability")
    public ResponseEntity<DriverResponse> updateAvailability(@PathVariable Long id,
                                                             @RequestBody DriverAvailabilityRequest request) {
        return driverService.updateAvailability(id, request);
    }

    @Operation(summary = "Delete a driver", description = "Deletes a driver by their ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return driverService.deleteDriver(id);
    }
}