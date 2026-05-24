
// --- entity/Customer.java ---
package com.cts.logichain360.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "customers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false, unique = true)
    private String gstNumber;

    private String email;
    private String shippingAddress;
    private String billingAddress;

    private Double creditLimit;
    private String paymentTerms;
}
