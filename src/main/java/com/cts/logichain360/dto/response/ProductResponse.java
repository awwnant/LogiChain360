package com.cts.logichain360.dto.response;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductResponse {
    private Long    productId;
    private String  productName;
    private Double  productPrice;
    private String  productDescription;
    private Integer quantity;

    //flattened vendor
    private Long   vendorId;
    private String vendorCompanyName;

    //flattened warehouseInfo
    private Long   warehouseId;
    private String warehouseCode;
    private String warehouseLocation;
}