package com.cts.logichain360.controller;

import com.cts.logichain360.dto.request.*;
import com.cts.logichain360.dto.response.ProductResponse;
import com.cts.logichain360.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody CreateProductRequest request) {
        return productService.createProduct(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() {
        return productService.getAllProducts();
    }

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<ProductResponse>> getByVendor(@PathVariable Long vendorId) {
        return productService.getProductsByVendor(vendorId);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<ProductResponse>> getByWarehouse(@PathVariable Long warehouseId) {
        return productService.getProductsByWarehouse(warehouseId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id,
                                                  @RequestBody UpdateProductRequest request) {
        return productService.updateProduct(id, request);
    }

    @PatchMapping("/{id}/quantity")
    public ResponseEntity<ProductResponse> adjustQuantity(@PathVariable Long id,
                                                          @RequestBody QuantityAdjustmentRequest request) {
        return productService.adjustQuantity(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }
}