package com.ecommercebackoffice.customer.dto;

import com.ecommercebackoffice.customer.entity.Customer;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({
        "customerId",
        "status",
        "updatedAt"
})
public class CustomerStatusUpdateResponse {

    private final Long customerId;
    private final String status;
    private final LocalDateTime updatedAt;

    public CustomerStatusUpdateResponse(Long customerId, String status, LocalDateTime updatedAt) {
        this.customerId = customerId;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    public static CustomerStatusUpdateResponse from(Customer customer) {
        return new CustomerStatusUpdateResponse(
                customer.getId(),
                customer.getStatus().getDescription(),
                customer.getUpdatedAt()
        );
    }
}
