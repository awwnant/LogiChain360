package com.cts.logichain360.dto.response;
import com.cts.logichain360.enums.Shift;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WarehouseManagerResponse {
    private Long   id;
    private Long   userId;
    private String userName;
    private String userPhone;
    private String employeeCode;
    private String designation;
    private Shift  shift;
    private String assignedWarehouseCode;
}