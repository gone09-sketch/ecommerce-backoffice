package com.ecommercebackoffice.customer.dto;

import com.ecommercebackoffice.customer.entity.Customer;
import com.ecommercebackoffice.customer.enums.CustomerStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CustomerUpdateResponse {

    private final Long id;
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final CustomerStatus customerStatus;

    private CustomerUpdateResponse(Long id, String name, String email, String phoneNumber, LocalDateTime createdAt, LocalDateTime updatedAt, CustomerStatus customerStatus) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.customerStatus = customerStatus;
    }

    public static CustomerUpdateResponse from(Customer customer) {
        return new CustomerUpdateResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getCreatedAt(),
                customer.getUpdatedAt(),
                customer.getCustomerStatus()
        );
    }
}
