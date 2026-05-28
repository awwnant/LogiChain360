package com.cts.logichain360.repository;

import com.cts.logichain360.entity.ProductWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ProductWarehouseRepository extends JpaRepository<ProductWarehouse, Long> {

    /** Today's 1:1: returns the one entry for a product. */
    Optional<ProductWarehouse> findByProduct_ProductId(Long productId);

    /** All entries at a given warehouse. */
    List<ProductWarehouse> findAllByWarehouse_Id(Long warehouseId);

    boolean existsByProduct_ProductId(Long productId);

    // Low-stock: stock as a percentage of maxStock is below the row's own rolPercent.
    // Derived methods can't compare two fields, so we use JPQL.
    @Query("""
           SELECT pw FROM ProductWarehouse pw
           WHERE pw.maxStock > 0
             AND (pw.stock * 100.0 / pw.maxStock) < pw.rolPercent
           """)
    List<ProductWarehouse> findAllBelowRol();

    @Query("""
           SELECT pw FROM ProductWarehouse pw
           WHERE pw.warehouse.id = :warehouseId
             AND pw.maxStock > 0
             AND (pw.stock * 100.0 / pw.maxStock) < pw.rolPercent
           """)
    List<ProductWarehouse> findAllBelowRolByWarehouse(Long warehouseId);
}