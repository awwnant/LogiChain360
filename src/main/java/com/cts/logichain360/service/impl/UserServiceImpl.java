package com.cts.logichain360.service.impl;

import com.cts.logichain360.dto.request.*;
import com.cts.logichain360.dto.response.*;
import com.cts.logichain360.entity.*;
import com.cts.logichain360.exception.UserAlreadyExistsException;
import com.cts.logichain360.repository.*;
import com.cts.logichain360.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final CustomerRepository customerRepo;
    private final VendorRepository vendorRepo;
    private final WarehouseManagerRepository wmRepo;
    private final DriverRepository driverRepo;
    private final PasswordEncoder encoder;

    @Override
    @Transactional
    public ResponseEntity<UserRegistrationResponse> registerUser(UserRegistrationRequest req) {
        if (userRepo.existsByPhone(req.getPhone())) {
            throw new UserAlreadyExistsException("Phone " + req.getPhone() + " is already registered.");
        }
        User saved = userRepo.save(User.builder()
                .name(req.getName())
                .phone(req.getPhone())
                .password(encoder.encode(req.getPassword()))
                .role(req.getRole())
                .build());
        ProfileResult p = createRoleProfile(saved, req);
        return new ResponseEntity<>(UserRegistrationResponse.builder()
                .userId(saved.getId())
                .name(saved.getName())
                .phone(saved.getPhone())
                .role(saved.getRole())
                .status(saved.getStatus())
                .roleProfileId(p.id())
                .roleProfileTable(p.table())
                .build(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest req) {
        return userRepo.findByPhoneAndIsDeletedFalse(req.getPhone())
                .filter(user -> encoder.matches(req.getPassword(), user.getPassword()))
                .map(user -> ResponseEntity.ok(LoginResponse.builder()
                        .userId(user.getId())
                        .name(user.getName())
                        .phone(user.getPhone())
                        .role(user.getRole())
                        .build()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @Override
    public ResponseEntity<UserResponse> getUserById(Long id) {
        return userRepo.findById(id)
                .map(u -> ResponseEntity.ok(toUserResponse(u)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userRepo.findAll().stream().map(this::toUserResponse).toList());
    }

    @Override
    @Transactional
    public ResponseEntity<UserResponse> updateUser(Long id, UpdateUserRequest req) {
        return userRepo.findById(id)
                .map(user -> {
                    if (req.getName()     != null) user.setName(req.getName());
                    if (req.getPassword() != null) user.setPassword(encoder.encode(req.getPassword()));
                    if (req.getPhone()    != null && !req.getPhone().equals(user.getPhone())) {
                        if (userRepo.existsByPhone(req.getPhone()))
                            throw new UserAlreadyExistsException("Phone " + req.getPhone() + " already in use.");
                        user.setPhone(req.getPhone());
                    }
                    return ResponseEntity.ok(toUserResponse(userRepo.save(user)));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @Transactional
    public ResponseEntity<UserResponse> updateUserStatus(Long id, UserStatusRequest req) {
        return userRepo.findById(id)
                .map(user -> {
                    user.setStatus(req.getStatus());
                    return ResponseEntity.ok(toUserResponse(userRepo.save(user)));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteUser(Long id) {
        return userRepo.findById(id)
                .map(user -> {
                    driverRepo.findByUser(user).ifPresent(driverRepo::delete);
                    vendorRepo.findByUser(user).ifPresent(vendorRepo::delete);
                    wmRepo.findByUser(user).ifPresent(wmRepo::delete);
                    userRepo.delete(user);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //Helpers 
    private record ProfileResult(Long id, String table) {}
    private ProfileResult createRoleProfile(User user, UserRegistrationRequest req) {
        return switch (user.getRole()) {
            case CUSTOMER -> {
                Customer c = customerRepo.save(Customer.builder()
                        .user(user).companyName(req.getCompanyName()).gstNumber(req.getGstNumber())
                        .email(req.getEmail()).shippingAddress(req.getShippingAddress())
                        .creditLimit(req.getCreditLimit()).paymentTerms(req.getPaymentTerms()).build());
                yield new ProfileResult(c.getId(), "customers");
            }
            case VENDOR -> {
                Vendor v = vendorRepo.save(Vendor.builder()
                        .user(user).companyName(req.getCompanyName()).gstNumber(req.getGstNumber())
                        .email(req.getEmail()).businessAddress(req.getBusinessAddress())
                        .contactPerson(req.getContactPerson()).paymentTerms(req.getPaymentTerms()).build());
                yield new ProfileResult(v.getId(), "vendors");
            }
            case WAREHOUSE_MANAGER -> {
                // No assignedWarehouse here , warehousemanagers are created first, then assigned via PATCH /api/v1/warehouse-managers/{id}/assign-warehouse with a warehouseId.
                WarehouseManager wm = wmRepo.save(WarehouseManager.builder()
                        .user(user)
                        .employeeCode(req.getEmployeeCode())
                        .designation(req.getDesignation())
                        .shift(req.getShift())
                        .build());
                yield new ProfileResult(wm.getId(), "warehouse_managers");
            }
            case DRIVER -> {
                Driver d = driverRepo.save(Driver.builder()
                        .user(user).licenseNumber(req.getLicenseNumber())
                        .licenseExpiry(req.getLicenseExpiry()).vehiclePreference(req.getVehiclePreference()).build());
                yield new ProfileResult(d.getId(), "drivers");
            }
        };
    }

    private UserResponse toUserResponse(User u) {
        return UserResponse.builder()
                .id(u.getId()).name(u.getName()).phone(u.getPhone())
                .role(u.getRole()).status(u.getStatus()).build();
    }
}