package com.cts.logichain360.dto.request;
import com.cts.logichain360.enums.VehiclePreference;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateDriverRequest {
    private String            licenseNumber;
    private LocalDate          licenseExpiry;
    private VehiclePreference vehiclePreference;
}