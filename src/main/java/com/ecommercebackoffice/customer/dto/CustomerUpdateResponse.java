package com.ecommercebackoffice.customer.dto;

import com.ecommercebackoffice.customer.entity.Customer;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({
        "customerId",
        "name",
        "email",
        "phoneNumber",
        "status",
        "createdAt",
        "updatedAt"
})
public class CustomerUpdateResponse {

    private final Long customerId;
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final String status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private CustomerUpdateResponse(Long customerId, String name, String email, String phoneNumber,
                                   String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CustomerUpdateResponse from(Customer customer) {
        return new CustomerUpdateResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getStatus().getDescription(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }
}
