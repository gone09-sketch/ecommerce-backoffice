package com.ecommercebackoffice.customer.dto;

import com.ecommercebackoffice.customer.entity.Customer;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CustomerGetListResponse {

    private final List<CustomerGetResponse> customerGetResponses;

    public CustomerGetListResponse(List<CustomerGetResponse> customerGetResponses) {
        this.customerGetResponses = customerGetResponses;
    }

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
    public static class CustomerGetResponse {

        private final Long customerId;
        private final String name;
        private final String email;
        private final String phoneNumber;
        private final String status;
        private final LocalDateTime createdAt;
        private final LocalDateTime updatedAt;

        private CustomerGetResponse(Long customerId, String name, String email, String phoneNumber,
                                    String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.customerId = customerId;
            this.name = name;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.status = status;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public static CustomerGetResponse from(Customer customer) {
            return new CustomerGetResponse(
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
}
