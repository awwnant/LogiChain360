package com.cts.logichain360.dto.request;

import com.cts.logichain360.enums.*;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationRequest {
    private String   name;
    private String   phone;
    private String   password;
    private UserRole role;

    // CUSTOMER & VENDOR shared
    private String companyName;
    private String gstNumber;
    private String email;

    // CUSTOMER specific
    private String shippingAddress;
    private String billingAddress;
    private Double creditLimit;
    private String       paymentTerms;

    // VENDOR specific
    private String businessAddress;
    private String contactPerson;

    // WAREHOUSE_MANAGER specific
    private String employeeCode;
    private String designation;
    private Shift  shift;
    private String assignedWarehouseCode;

    // DRIVER specific
    private String            licenseNumber;
    private LocalDate          licenseExpiry;
    private VehiclePreference vehiclePreference;
}