package com.cts.logichain360.dto.request;
import com.cts.logichain360.enums.Shift;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateWarehouseManagerRequest {
    private String employeeCode;
    private String designation;
    private Shift  shift;
    private String assignedWarehouseCode;
}