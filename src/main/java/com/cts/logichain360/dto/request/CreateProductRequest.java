package com.cts.logichain360.dto.request;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateProductRequest {
    private String  productName;
    private Double  productPrice;
    private String  productDescription;
    private Integer quantity;
    private Long    vendorId;
    private Long    warehouseId;
}