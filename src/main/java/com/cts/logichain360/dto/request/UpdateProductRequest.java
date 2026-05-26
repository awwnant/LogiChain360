package com.cts.logichain360.dto.request;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateProductRequest {
    private String  productName;
    private Double  productPrice;
    private String  productDescription;
    private Integer quantity;
    // vendor and warehouse cannot be reassigned after creation for now so no vendor and warehouse fields
}