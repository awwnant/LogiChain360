package com.cts.logichain360.entity;

import com.cts.logichain360.enums.UserRole;
import com.cts.logichain360.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity @Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder

//Overriding jpa delete to set field as true false
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ?")
//automatically will filter all queries where isDeleted is false
@Where(clause = "is_deleted = false")

public class User extends SoftDeletableEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;


//    @Column(name = "is_deleted", nullable = false)
//    @Builder.Default
//    private boolean isDeleted = false;

}
