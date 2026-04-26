package com.ecommercebackoffice.customer.entity;

import com.ecommercebackoffice.config.BaseEntity;
import com.ecommercebackoffice.customer.dto.CustomerUpdateRequest;
import com.ecommercebackoffice.customer.dto.CustomerUpdateResponse;
import com.ecommercebackoffice.customer.enums.CustomerStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.SoftDelete;

import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SoftDelete(columnName = "is_deleted")
@SQLRestriction("is_deleted = false")
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


    public Customer(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = CustomerStatus.ACTIVE;
    }


    public void update(CustomerUpdateRequest request) {
        this.name = request.getName();
        this.email = request.getEmail();
        this.phoneNumber = request.getPhoneNumber();
    }

    public void updateStatus(CustomerStatus status) {
        this.status = status;
    }

}
