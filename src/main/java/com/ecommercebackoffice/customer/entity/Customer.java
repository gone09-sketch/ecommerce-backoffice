package com.ecommercebackoffice.customer.entity;

import com.ecommercebackoffice.config.BaseEntity;
import com.ecommercebackoffice.customer.enums.CustomerStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatus status;

    private LocalDateTime removedAt;

    private String removedReason;


    @SQLRestriction("is_deleted = false")// 참일때만 보임
    @Column(name = "isdeleted", nullable = false)
    private Boolean isDeleted;



    public Customer(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = CustomerStatus.ACTIVE;
        this.isDeleted = false;
    }

    public void update(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }


    public void updateStatus(CustomerStatus status) {
        this.status = status;
    }

    @SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ?")
    public void delete(String removedReason) {
        this.isDeleted = true;
        this.removedAt = LocalDateTime.now();
        this.removedReason = removedReason;
    }
}
