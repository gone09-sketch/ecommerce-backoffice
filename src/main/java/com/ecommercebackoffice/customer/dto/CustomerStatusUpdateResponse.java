package com.ecommercebackoffice.customer.dto;

import com.ecommercebackoffice.customer.entity.Customer;
import com.ecommercebackoffice.customer.enums.CustomerStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CustomerStatusUpdateResponse {

    private final Long customerId;
    private final CustomerStatus customerStatus;
    private final LocalDateTime updatedAt;

    public CustomerStatusUpdateResponse(Long customerId, CustomerStatus customerStatus, LocalDateTime updatedAt) {
        this.customerId = customerId;
        this.customerStatus = customerStatus;
        this.updatedAt = updatedAt;
    }

    public static CustomerStatusUpdateResponse from(Customer customer) {
        return new CustomerStatusUpdateResponse(
                customer.getId(),
                customer.getCustomerStatus(),
                customer.getUpdatedAt()
        );
    }
}
