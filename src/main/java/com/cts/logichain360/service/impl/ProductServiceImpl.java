package com.cts.logichain360.service.impl;

import com.cts.logichain360.dto.request.*;
import com.cts.logichain360.dto.response.ProductResponse;
import com.cts.logichain360.entity.Product;
import com.cts.logichain360.entity.Vendor;
import com.cts.logichain360.exception.ResourceNotFoundException;
import com.cts.logichain360.exception.UserAlreadyExistsException;
import com.cts.logichain360.repository.ProductRepository;
import com.cts.logichain360.repository.VendorRepository;
import com.cts.logichain360.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final VendorRepository  vendorRepo;

    @Override
    @Transactional
    public ResponseEntity<ProductResponse> createProduct(CreateProductRequest req) {
        log.info("Creating product '{}' for vendor {}", req.getProductName(), req.getVendorId());

        Vendor vendor = vendorRepo.findById(req.getVendorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vendor " + req.getVendorId() + " not found."));

        if (productRepo.existsByProductNameAndVendor_Id(req.getProductName(), vendor.getId())) {
            log.warn("Duplicate product name '{}' for vendor {}", req.getProductName(), vendor.getId());
            throw new UserAlreadyExistsException(
                    "Vendor " + vendor.getId() + " already has a product named '"
                            + req.getProductName() + "'.");
        }

        Product saved = productRepo.save(Product.builder()
                .productName(req.getProductName())
                .productPrice(req.getProductPrice())
                .productDescription(req.getProductDescription())
                .vendor(vendor)
                .build());

        log.info("Created product id={} for vendor {}", saved.getProductId(), vendor.getId());
        return new ResponseEntity<>(toResponse(saved), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ProductResponse> getProductById(Long id) {
        log.debug("Fetching product id={}", id);
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product " + id + " not found."));
        return ResponseEntity.ok(toResponse(p));
    }

    @Override
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        log.debug("Fetching all products");
        return ResponseEntity.ok(productRepo.findAll().stream().map(this::toResponse).toList());
    }

    @Override
    public ResponseEntity<List<ProductResponse>> getProductsByVendor(Long vendorId) {
        log.debug("Fetching all products for vendor={}", vendorId);
        if (!vendorRepo.existsById(vendorId)) {
            throw new ResourceNotFoundException("Vendor " + vendorId + " not found.");
        }
        return ResponseEntity.ok(productRepo.findAllByVendor_Id(vendorId)
                .stream().map(this::toResponse).toList());
    }

    @Override
    @Transactional
    public ResponseEntity<ProductResponse> updateProduct(Long id, UpdateProductRequest req) {
        log.info("Updating product id={}", id);
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

        return ResponseEntity.ok(toResponse(productRepo.save(p)));
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteProduct(Long id) {
        log.info("Deleting product id={}", id);
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
                .vendorId(p.getVendor().getId())
                .vendorCompanyName(p.getVendor().getCompanyName())
                .build();
    }
}