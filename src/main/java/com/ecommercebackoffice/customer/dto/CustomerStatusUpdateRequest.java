package com.ecommercebackoffice.customer.dto;

import com.ecommercebackoffice.customer.enums.CustomerStatus;
import lombok.Getter;

@Getter
public class CustomerStatusUpdateRequest {

    private CustomerStatus status;
}
