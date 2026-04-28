package com.ecommercebackoffice.customer.entity;

import com.ecommercebackoffice.config.BaseEntity;
import com.ecommercebackoffice.customer.dto.CustomerUpdateRequest;
import com.ecommercebackoffice.customer.enums.CustomerStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public Customer(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = CustomerStatus.ACTIVE;
    }


    public void update(String name, String email, String phoneNumber) {
        if (name != null) this.name = name;
        if (email != null) this.email = email;
        if (phoneNumber != null) this.phoneNumber = phoneNumber;
    }

    public void updateStatus(CustomerStatus status) {
        this.status = status;
    }

}
