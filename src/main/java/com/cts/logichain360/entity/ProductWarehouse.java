package com.cts.logichain360.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_warehouses",
    // 1:1 for now : a product can only be launched at one warehouse.
    // If we later scale to many to many then drop this constraint
    uniqueConstraints = @UniqueConstraint(columnNames = "product_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductWarehouse extends SoftDeletableEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Integer maxStock;

    // ROL as percentage of maxStock. e.g. 40 means restock when stock < 40% of maxStock.
    @Column(name = "rol_percent", nullable = false)
    private Double rolPercent;

    //Derived: true when current stock is below the reorder level. Not persisted.
    @Transient
    public boolean isBelowRol() {
        if (maxStock == null || maxStock == 0 || rolPercent == null) return false;
        double stockPercent = (stock.doubleValue() / maxStock.doubleValue()) * 100.0;
        return stockPercent < rolPercent;
    }
}