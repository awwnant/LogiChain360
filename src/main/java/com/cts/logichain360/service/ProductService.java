package com.cts.logichain360.service;

import com.cts.logichain360.dto.request.*;
import com.cts.logichain360.dto.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface ProductService {
    ResponseEntity<ProductResponse> createProduct(CreateProductRequest request);
    ResponseEntity<ProductResponse> getProductById(Long id);
    ResponseEntity<List<ProductResponse>> getAllProducts();
    ResponseEntity<List<ProductResponse>> getProductsByVendor(Long vendorId);
    ResponseEntity<List<ProductResponse>> getProductsByWarehouse(Long warehouseId);
    ResponseEntity<ProductResponse> updateProduct(Long id, UpdateProductRequest request);
    ResponseEntity<ProductResponse> adjustQuantity(Long id, QuantityAdjustmentRequest request);
    ResponseEntity<Void> deleteProduct(Long id);
}