
package com.cts.logichain360.entity;

import com.cts.logichain360.enums.Shift;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "warehouse_managers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WarehouseManager extends SoftDeletableEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(unique = true)
    private String employeeCode;

    private String designation;

    @Enumerated(EnumType.STRING)
    private Shift shift;
    
    
    //One warehouse one manager
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_warehouse_id", unique = true)
    private Warehouse assignedWarehouse;
}