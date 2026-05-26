package com.cts.logichain360.entity;

import com.cts.logichain360.enums.VehiclePreference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity @Table(name = "drivers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Driver extends SoftDeletableEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String licenseNumber;
    private LocalDate licenseExpiry;

    @Enumerated(EnumType.STRING)
    private VehiclePreference vehiclePreference;

    @Builder.Default
    private Boolean available = true;
}
