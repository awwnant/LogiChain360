package com.cts.logichain360.service.impl;

import com.cts.logichain360.dto.request.*;
import com.cts.logichain360.dto.response.ProductResponse;
import com.cts.logichain360.entity.Product;
import com.cts.logichain360.entity.Vendor;
import com.cts.logichain360.entity.Warehouse;
import com.cts.logichain360.exception.ResourceNotFoundException;
import com.cts.logichain360.exception.UserAlreadyExistsException;
import com.cts.logichain360.repository.*;
import com.cts.logichain360.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository   productRepo;
    private final VendorRepository    vendorRepo;
    private final WarehouseRepository warehouseRepo;

    @Override
    @Transactional
    public ResponseEntity<ProductResponse> createProduct(CreateProductRequest req) {
        Vendor vendor = vendorRepo.findById(req.getVendorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vendor " + req.getVendorId() + " not found."));

        Warehouse warehouse = warehouseRepo.findById(req.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Warehouse " + req.getWarehouseId() + " not found."));

        if (productRepo.existsByProductNameAndVendor_Id(req.getProductName(), vendor.getId())) {
            throw new UserAlreadyExistsException(
                    "Vendor " + vendor.getId() + " already has a product named '"
                            + req.getProductName() + "'.");
        }

        Product saved = productRepo.save(Product.builder()
                .productName(req.getProductName())
                .productPrice(req.getProductPrice())
                .productDescription(req.getProductDescription())
                .quantity(req.getQuantity() == null ? 0 : req.getQuantity())
                .vendor(vendor)
                .warehouse(warehouse)
                .build());

        return new ResponseEntity<>(toResponse(saved), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ProductResponse> getProductById(Long id) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product " + id + " not found."));
        return ResponseEntity.ok(toResponse(p));
    }

    @Override
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productRepo.findAll().stream().map(this::toResponse).toList());
    }

    @Override
    public ResponseEntity<List<ProductResponse>> getProductsByVendor(Long vendorId) {
        if (!vendorRepo.existsById(vendorId)) {
            throw new ResourceNotFoundException("Vendor " + vendorId + " not found.");
        }
        return ResponseEntity.ok(productRepo.findAllByVendor_Id(vendorId)
                .stream().map(this::toResponse).toList());
    }

    @Override
    public ResponseEntity<List<ProductResponse>> getProductsByWarehouse(Long warehouseId) {
        if (!warehouseRepo.existsById(warehouseId)) {
            throw new ResourceNotFoundException("Warehouse " + warehouseId + " not found.");
        }
        return ResponseEntity.ok(productRepo.findAllByWarehouse_Id(warehouseId)
                .stream().map(this::toResponse).toList());
    }

    @Override
    @Transactional
    public ResponseEntity<ProductResponse> updateProduct(Long id, UpdateProductRequest req) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product " + id + " not found."));

        if (req.getProductName() != null && !req.getProductName().equals(p.getProductName())) {
            if (productRepo.existsByProductNameAndVendor_Id(
                    req.getProductName(), p.getVendor().getId())) {
                throw new UserAlreadyExistsException(
                        "Vendor " + p.getVendor().getId() + " already has a product named '"
                                + req.getProductName() + "'.");
            }
            p.setProductName(req.getProductName());
        }
        if (req.getProductPrice()       != null) p.setProductPrice(req.getProductPrice());
        if (req.getProductDescription() != null) p.setProductDescription(req.getProductDescription());
        if (req.getQuantity()           != null) p.setQuantity(req.getQuantity());

        return ResponseEntity.ok(toResponse(productRepo.save(p)));
    }

    @Override
    @Transactional
    public ResponseEntity<ProductResponse> adjustQuantity(Long id, QuantityAdjustmentRequest req) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product " + id + " not found."));

        int newQty = p.getQuantity() + req.getDelta();
        if (newQty < 0) {
            throw new IllegalArgumentException(
                    "Cannot reduce quantity below zero. Current=" + p.getQuantity()
                            + ", delta=" + req.getDelta());
        }
        p.setQuantity(newQty);
        return ResponseEntity.ok(toResponse(productRepo.save(p)));
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteProduct(Long id) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product " + id + " not found."));
        productRepo.delete(p);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private ProductResponse toResponse(Product p) {
        return ProductResponse.builder()
                .productId(p.getProductId())
                .productName(p.getProductName())
                .productPrice(p.getProductPrice())
                .productDescription(p.getProductDescription())
                .quantity(p.getQuantity())
                .vendorId(p.getVendor().getId())
                .vendorCompanyName(p.getVendor().getCompanyName())
                .warehouseId(p.getWarehouse().getId())
                .warehouseCode(p.getWarehouse().getWarehouseCode())
                .warehouseLocation(p.getWarehouse().getLocation())
                .build();
    }
}