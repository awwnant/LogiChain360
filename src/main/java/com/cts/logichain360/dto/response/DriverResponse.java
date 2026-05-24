package com.cts.logichain360.dto.response;
import com.cts.logichain360.enums.VehiclePreference;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DriverResponse {
    private Long              id;
    private Long              userId;
    private String            userName;
    private String            userPhone;
    private String            licenseNumber;
    private LocalDate          licenseExpiry;
    private VehiclePreference vehiclePreference;
    private Boolean            available;
}